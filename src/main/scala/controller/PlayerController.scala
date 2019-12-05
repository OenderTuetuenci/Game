package controller

import controller.BoardController
import controller.GameController
import model._
import util.Observable

import scala.util.control.Breaks.{break, breakable}

class PlayerController(gameController: GameController){
  def createPlayers(playerNames:Array[String],npcNames:Array[String]): Vector[Player] = {
    var players:Vector[Player] = Vector()
    for(player <-playerNames)
      players = players :+ Player(player)
    for(npc <-npcNames)
      players = players :+ Player(npc,isNpc = true)
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
                            gameController.print(playerPaysHyptohekOnStreetEvent(players(isturn), board(i).asInstanceOf[Street]))
                        }
                    }
                case s: Trainstation =>
                    if (board(i).asInstanceOf[Trainstation].owner == isturn) {
                        // wenn er die hypothek zahlen kann tut er dies
                        if (s.hypothek && players(isturn).money > s.price) {
                            board = board.updated(i, board(i).asInstanceOf[Trainstation].payHypothek())
                            players = players.updated(isturn, players(isturn).decMoney(s.price))
                            gameController.print(playerPaysHyptohekOnTrainstationEvent(players(isturn), board(i).asInstanceOf[Trainstation]))
                        }
                    }
                case _ =>
            }
        }
    }
    def checkDept(playerState: Vector[Player]):(Vector[Cell],Vector[Player]) = {
        // wenn spieler im minus its wird
        // so lange verkauft bis im plus oder nichts mehr verkauft wurde dann gameover
        var board = gameController.board
        val isturn = gameController.isturn
        var players = playerState
        if (players(isturn).money <= 0) {
            gameController.print(playerHasDeptEvent(players(isturn)))
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
                                        gameController.print(playerUsesHyptohekOnStreetEvent(players(isturn), board(i).asInstanceOf[Street]))
                                        actionDone = true
                                    } else {
                                        //sonst straße verkaufen an bank todo später an spieler
                                        board = board.updated(i, board(i).asInstanceOf[Buyable].setOwner(-1))
                                        players = players.updated(isturn, players(isturn).incMoney(s.price))
                                        players = players.updated(isturn, players(isturn).sellStreet(i))
                                        gameController.print(playerSellsStreetEvent(players(isturn), board(i).asInstanceOf[Street]))
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
                gameController.print(brokeEvent(players(isturn)))
            }
        }
        (board,players)
    }

    def buy(field: Buyable): (Vector[Cell],Vector[Player]) = {
        var board = gameController.board
        var players = gameController.players
        val isturn = gameController.isturn
        if (players(isturn).money >= field.price) {
            players = players.updated(isturn, players(isturn).decMoney(field.price))
            players = players.updated(isturn,players(isturn).buyStreet(players(isturn).position))
            // spieler.besitz add streetnr
            board = board.updated(players(isturn).position, field.setOwner(isturn))
        }
        gameController.print(buyStreetEvent(players(isturn), field))
        (board,players)
    }

    def movePlayer(sumDiceThrow: Int): Vector[Player] = {
        // spieler bewegen
        val board = gameController.board
        var players = gameController.players
        val isturn = gameController.isturn
        val boardController = gameController.boardController

        players = players.updated(isturn, players(isturn).move(sumDiceThrow))
        // schauen ob über los gegangen
        if (players(isturn).position >= 40) {
            gameController.print(playerWentOverGoEvent(players(isturn)))
            players = players.updated(isturn, players(isturn).moveBack(40))
        }
        // neue position ausgeben
        gameController.print(playerMoveEvent(players(isturn)))
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

    def payRent(field: Buyable): (Vector[Cell],Vector[Player]) = {
      var players = gameController.players
      val isturn = gameController.isturn
      // mietpreis holen
      val rent = field.rent
      //miete abziehen
      println(players(isturn).money)
      players = players.updated(isturn, players(isturn).decMoney(rent))
      println(players(isturn).money)
      players = players.updated(field.owner, players(field.owner).incMoney(rent))
      gameController.print(payRentEvent(players(isturn), players(field.owner)))
      // schauen ob player ins minus gekommen ist
      checkDept(players)
  }


}
