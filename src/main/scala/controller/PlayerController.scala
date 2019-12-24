package controller

import model._
import scalafx.scene.image.{Image, ImageView}

import scala.util.control.Breaks.{break, breakable}

class PlayerController(gameController: GameController) {

    def createPlayers(playerNames: Vector[String], npcNames: Vector[String]): Vector[Player] = {
        var players: Vector[Player] = Vector()
        var i = 0
        for (name <- playerNames) {
            val imgView = new ImageView(new Image(gameController.playerFigures(i),
                50,
                50,
                true,
                true))
            imgView.setId("player" + i)
            players = players :+ Player(name, strategy = HumanStrategy(gameController), figure = imgView)
            i += 1

        }
        i = 0
        for (name <- npcNames) {
            ///////////////
            // 1. verfügbare figur nehmen
            // todo try figure if is empty take picture for more npc than playerfigures nein
            //            var f1 = ""
            //            try {
            //                f1 = Some(gameController.remainingFiguresToPick.head).toString()
            //            }
            //            catch {
            //                case e: Exception => f1 = "Hut"
            //            }
            //            print("fi" + f1)
            //////////////////////
            val figure = gameController.remainingFiguresToPick.head
            val imgPath = figure match {
                case "Hut" => "file:images/Hut.jpg"
                case "Fingerhut" => "file:images/Fingerhut.jpg"
                case "Schubkarre" => "file:images/Schubkarre.jpg"
                case "Schuh" => "file:images/Schuh.jpg"
                case "Hund" => "file:images/Hund.jpg"
                case "Auto" => "file:images/Auto.jpg"
                case "Bügeleisen" => "file:images/Buegeleisen.jpg"
                case "Fingerhut" => "file:images/Fingerhut.jpg"
                case "Schiff" => "file:images/Schiff.jpg"
            }
            // ausgewählte figur aus der auswahl nehmen
            gameController.remainingFiguresToPick = gameController.remainingFiguresToPick.filterNot(elm => elm == figure)
            /////////////////////////////
            val imgView = new ImageView(new Image(imgPath,
                50,
                50,
                true,
                true))
            players = players :+ Player(name, strategy = NPCStrategy(gameController), figure = imgView)
        }
        players
    }

    def checkHypothek(): Unit = {
        var board = gameController.board
        var players = gameController.players
        val currentPlayer = gameController.currentPlayer
        // in allen straßen des spielers suchen ob hypothek vorliegt
        // todo lieber feld von besitz beim spieler anlegen damit man nicht durch alle felder muss
        for (i <- board.indices) {
            board(i) match {
                case s: Street =>
                    if (board(i).asInstanceOf[Street].owner == currentPlayer) {
                        // wenn er die hypothek zahlen kann tut er dies
                        if (s.mortgage && players(currentPlayer).money > s.price) {
                            board = board.updated(i, board(i).asInstanceOf[Street].payMortgage)
                            players = players.updated(currentPlayer, players(currentPlayer).decMoney(s.price))
                            gameController.printFun(playerPaysHyptohekOnStreetEvent(players(currentPlayer), board(i).asInstanceOf[Street]))
                        }
                    }
                case _ =>
            }
        }
    }

    def buy(field: Buyable): (Vector[Cell], Vector[PlayerInterface]) = {
        var board = gameController.board
        var players = gameController.players
        val currentPlayer = gameController.currentPlayer
        if (players(currentPlayer).money >= field.price) {
            players = players.updated(currentPlayer, players(currentPlayer).decMoney(field.price))
            players = players.updated(currentPlayer, players(currentPlayer).buyStreet(players(currentPlayer).position))
            // spieler.besitz add streetnr
            board = board.updated(players(currentPlayer).position, field.setOwner(currentPlayer))
        }
        gameController.printFun(buyStreetEvent(players(currentPlayer), field))
        (board, players)
    }

    def wuerfeln: (Int, Int, Boolean) = {
        val roll1 = gameController.dice.roll
        val roll2 = gameController.dice.roll
        var pasch = false
        if (gameController.dice.checkPash(roll1, roll2)) {
            pasch = true
        }
        gameController.notifyObservers(UpdateGuiDiceLabelEvent(roll1, roll2, pasch))

        (roll1, roll2, pasch)
    }

    def movePlayer(sumDiceThrow: Int): Vector[PlayerInterface] = {
        // spieler bewegen
        import gameController._
        players = players.updated(currentPlayer, players(currentPlayer).move(sumDiceThrow))
        println("sumDiceThrow = " + sumDiceThrow)
        // schauen ob über los gegangen todo wenn spieler auf jail kommt und pasch gewuerfelt hat
        if (players(currentPlayer).position >= 40) {
            gameController.printFun(playerWentOverGoEvent(players(currentPlayer)))
            if (!(players(currentPlayer).position == 0)) // falls spieler nicht auf los sonst 2 dialoge
                gameController.notifyObservers(OpenPlayerPassedGoDialog())
            players = players.updated(currentPlayer, players(currentPlayer).moveBack(40))
        }
        ////////////MoveplayerAfterRollDice////////////////
        gameController.notifyObservers(MovePlayerFigureEvent(gameController.players(currentPlayer).figure,
            gameController.fieldCoordsX(players(currentPlayer).position),
            gameController.fieldCoordsY(players(currentPlayer).position)))

        // neue position ausgeben
        gameController.printFun(playerMoveEvent(players(currentPlayer)))
        // aktion fuer betretetenes feld ausloesen
        val field = board(players(currentPlayer).position)
        field match {
            case e: Buyable => boardController.activateStreet(e.asInstanceOf[Buyable])
            case e: Los => boardController.activateStart(e.asInstanceOf[Los])
            case e: Eventcell => boardController.activateChance(e.asInstanceOf[Eventcell])
            case e: CommunityChest => boardController.activateCommunityChest(e.asInstanceOf[CommunityChest])
            case e: Jail => boardController.activateVisitJail(e.asInstanceOf[Jail])
            case e: IncomeTax => boardController.activateIncomeTax(e.asInstanceOf[IncomeTax])
            case e: FreiParken => boardController.activateFreiParken(e.asInstanceOf[FreiParken])
            case e: GoToJail => boardController.activateJail(e.asInstanceOf[GoToJail])
            case e: Zusatzsteuer => boardController.activateLuxuaryTax(e.asInstanceOf[Zusatzsteuer])
            case _ => throw new UnsupportedOperationException
        }
        players
    }

    def payRent(field: Buyable): (Vector[Cell], Vector[PlayerInterface]) = {
        var players = gameController.players
        val currentPlayer = gameController.currentPlayer
        // mietpreis holen
        val rent = field.rent
        //miete abziehen
        println(players(currentPlayer).money)
        players = players.updated(currentPlayer, players(currentPlayer).decMoney(rent))
        println(players(currentPlayer).money)
        players = players.updated(field.owner, players(field.owner).incMoney(rent))
        gameController.printFun(payRentEvent(players(currentPlayer), players(field.owner)))
        // schauen ob player ins minus gekommen ist
        //todo checkDept(players)
        (gameController.board, players)
    }

    def checkDept(playerState: Vector[Player]): (Vector[Cell], Vector[Player]) = {
        // wenn spieler im minus its wird
        // so lange verkauft bis im plus oder nichts mehr verkauft wurde dann gameover
        var board = gameController.board
        val currentPlayer = gameController.currentPlayer
        var players = playerState
        if (players(currentPlayer).money <= 0) {
            gameController.printFun(playerHasDeptEvent(players(currentPlayer)))
            var actionDone = false
            breakable { // break wenn player plus -> breakable from scala.util.
                do {
                    for (i <- board.indices) {
                        actionDone = false
                        board(i) match {
                            //todo straßen, karten, ... verkaufen, später an spieler oder bank
                            case s: Street =>
                                // erst auf hypothek setzen dann an bank verkaufen
                                if (board(i).asInstanceOf[Street].owner == currentPlayer) {
                                    // todo hotels dann haeuser zuerst verkaufen
                                    // strasse mit hypothek belasten
                                    if (!s.mortgage) {
                                        board = board.updated(i, board(i).asInstanceOf[Buyable].getMortgage)
                                        players = players.updated(currentPlayer, players(currentPlayer).incMoney(s.price))
                                        gameController.printFun(playerUsesHyptohekOnStreetEvent(players(currentPlayer), board(i).asInstanceOf[Street]))
                                        actionDone = true
                                    } else {
                                        //sonst straße verkaufen an bank todo später an spieler
                                        board = board.updated(i, board(i).asInstanceOf[Buyable].setOwner(-1))
                                        players = players.updated(currentPlayer, players(currentPlayer).incMoney(s.price))
                                        players = players.updated(currentPlayer, players(currentPlayer).sellStreet(i))
                                        gameController.printFun(playerSellsStreetEvent(players(currentPlayer), board(i).asInstanceOf[Street]))
                                        actionDone = true
                                    }
                                }
                            case _ => // TODO restliche felder aber besser andere lösung für felder finden da alles redundant
                        }
                    }
                    if (players(currentPlayer).money > 0) break // sobald alle schulden bezahlt sind nichtmehr verkaufen
                } while (actionDone)
            }

            // wenn immernoch pleite dann game over oder todo "declare bankrupt" später
            if (players(currentPlayer).money <= 0) {
                //players = players.filterNot(o => o == players(currentPlayer))// spieler aus dem spiel nehmen
                gameController.printFun(brokeEvent(players(currentPlayer)))
            }
        }
        (board, players)
    }


}
