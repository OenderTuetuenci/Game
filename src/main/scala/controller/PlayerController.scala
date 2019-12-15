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
        val isturn = gameController.isturn
        // in allen straßen des spielers suchen ob hypothek vorliegt
        // todo lieber feld von besitz beim spieler anlegen damit man nicht durch alle felder muss
        for (i <- board.indices) {
            board(i) match {
                case s: Street =>
                    if (board(i).asInstanceOf[Street].owner == isturn) {
                        // wenn er die hypothek zahlen kann tut er dies
                        if (s.mortgage && players(isturn).money > s.price) {
                            board = board.updated(i, board(i).asInstanceOf[Street].payMortgage)
                            players = players.updated(isturn, players(isturn).decMoney(s.price))
                            gameController.printFun(playerPaysHyptohekOnStreetEvent(players(isturn), board(i).asInstanceOf[Street]))
                        }
                    }
                case s: Trainstation =>
                    if (board(i).asInstanceOf[Trainstation].owner == isturn) {
                        // wenn er die hypothek zahlen kann tut er dies
                        if (s.mortgage && players(isturn).money > s.price) {
                            board = board.updated(i, board(i).asInstanceOf[Trainstation].payHypothek())
                            players = players.updated(isturn, players(isturn).decMoney(s.price))
                            gameController.printFun(playerPaysHyptohekOnTrainstationEvent(players(isturn), board(i).asInstanceOf[Trainstation]))
                        }
                    }
                case _ =>
            }
        }
    }

    def buy(field: Buyable): (Vector[Cell], Vector[Player]) = {
        var board = gameController.board
        var players = gameController.players
        val isturn = gameController.isturn
        if (players(isturn).money >= field.price) {
            players = players.updated(isturn, players(isturn).decMoney(field.price))
            players = players.updated(isturn, players(isturn).buyStreet(players(isturn).position))
            // spieler.besitz add streetnr
            board = board.updated(players(isturn).position, field.setOwner(isturn))
        }
        gameController.printFun(buyStreetEvent(players(isturn), field))
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

    def movePlayer(sumDiceThrow: Int): Vector[Player] = {
        // spieler bewegen
        val board = gameController.board
        var players = gameController.players
        val isturn = gameController.isturn
        val boardController = gameController.boardController

        players = players.updated(isturn, players(isturn).move(sumDiceThrow))
        println("sumDiceThrow = " + sumDiceThrow)
        // schauen ob über los gegangen todo wenn spieler auf jail kommt und pasch gewuerfelt hat
        if (players(isturn).position >= 40) {
            gameController.printFun(playerWentOverGoEvent(players(isturn)))
            players = players.updated(isturn, players(isturn).moveBack(40))
        }
        ////////////MoveplayerAfterRollDice////////////////
        gameController.notifyObservers(MovePlayerFigureEvent(gameController.players(isturn).figure,
            gameController.fieldCoordsX(players(isturn).position),
            gameController.fieldCoordsY(players(isturn).position)))

        // neue position ausgeben
        gameController.printFun(playerMoveEvent(players(isturn)))
        // aktion fuer betretetenes feld ausloesen
        /*val field = board(players(isturn).position)
        field match {
            case e: Los => boardController.activateStart(e.asInstanceOf[Los])
            case e: Street => boardController.activateStreet(e.asInstanceOf[Street])
            case e: Trainstation => boardController.activateTrainstation(e.asInstanceOf[Trainstation])
            case e: Eventcell => boardController.activateEvent(e.asInstanceOf[Eventcell])
            case e: CommunityChest => boardController.activateCommunityChest(e.asInstanceOf[CommunityChest])
            case e: IncomeTax => boardController.activateIncomeTax(e.asInstanceOf[IncomeTax])
            case e: Elektrizitaetswerk => boardController.activateElektrizitaetswerk(e.asInstanceOf[Elektrizitaetswerk])
            case e: Wasserwerk => boardController.activateWasserwerk(e.asInstanceOf[Wasserwerk])
            case e: Zusatzsteuer => boardController.activateZusatzsteuer(e.asInstanceOf[Zusatzsteuer])
            case e: FreiParken => boardController.activateFreiParken(e.asInstanceOf[FreiParken])
            case e: GoToJail => boardController.activateGoToJail(e.asInstanceOf[GoToJail])
            case e: Jail => boardController.activateJail(e.asInstanceOf[Jail])
            case _ =>
        }*/
        players
    }

    def payRent(field: Buyable): (Vector[Cell], Vector[Player]) = {
        var players = gameController.players
        val isturn = gameController.isturn
        // mietpreis holen
        val rent = field.rent
        //miete abziehen
        println(players(isturn).money)
        players = players.updated(isturn, players(isturn).decMoney(rent))
        println(players(isturn).money)
        players = players.updated(field.owner, players(field.owner).incMoney(rent))
        gameController.printFun(payRentEvent(players(isturn), players(field.owner)))
        // schauen ob player ins minus gekommen ist
        checkDept(players)
    }

    def checkDept(playerState: Vector[Player]): (Vector[Cell], Vector[Player]) = {
        // wenn spieler im minus its wird
        // so lange verkauft bis im plus oder nichts mehr verkauft wurde dann gameover
        var board = gameController.board
        val isturn = gameController.isturn
        var players = playerState
        if (players(isturn).money <= 0) {
            gameController.printFun(playerHasDeptEvent(players(isturn)))
            var actionDone = false
            breakable { // break wenn player plus -> breakable from scala.util.
                do {
                    for (i <- board.indices) {
                        actionDone = false
                        board(i) match {
                            //todo straßen, karten, ... verkaufen, später an spieler oder bank
                            case s: Street =>
                                // erst auf hypothek setzen dann an bank verkaufen
                                if (board(i).asInstanceOf[Street].owner == isturn) {
                                    // todo hotels dann haeuser zuerst verkaufen
                                    // strasse mit hypothek belasten
                                    if (!s.mortgage) {
                                        board = board.updated(i, board(i).asInstanceOf[Buyable].getMortgage)
                                        players = players.updated(isturn, players(isturn).incMoney(s.price))
                                        gameController.printFun(playerUsesHyptohekOnStreetEvent(players(isturn), board(i).asInstanceOf[Street]))
                                        actionDone = true
                                    } else {
                                        //sonst straße verkaufen an bank todo später an spieler
                                        board = board.updated(i, board(i).asInstanceOf[Buyable].setOwner(-1))
                                        players = players.updated(isturn, players(isturn).incMoney(s.price))
                                        players = players.updated(isturn, players(isturn).sellStreet(i))
                                        gameController.printFun(playerSellsStreetEvent(players(isturn), board(i).asInstanceOf[Street]))
                                        actionDone = true
                                    }
                                }
                            case _ => // TODO restliche felder aber besser andere lösung für felder finden da alles redundant
                        }
                    }
                    if (players(isturn).money > 0) break // sobald alle schulden bezahlt sind nichtmehr verkaufen
                } while (actionDone)
            }

            // wenn immernoch pleite dann game over oder todo "declare bankrupt" später
            if (players(isturn).money <= 0) {
                //players = players.filterNot(o => o == players(isturn))// spieler aus dem spiel nehmen
                gameController.printFun(brokeEvent(players(isturn)))
            }
        }
        (board, players)
    }


}
