package controller

import controller.BoardController
import controller.GameController
import model._
import util.Observable

import scala.util.control.Breaks.{break, breakable}

class PlayerController(){
  def movePlayer(roll:Int,jail:Boolean,player: Player): Player ={
    var updated = player
    if(jail)
      updated = player.moveToJail
    else
      updated = player.move(roll)
    if(updated.position >= 40)
      updated = updated.moveBack(40)
    updated
  }
  def createPlayers(playerNames:Array[String],npcNames:Array[String]): Vector[Player] = {
    var players:Vector[Player] = Vector()
    for(player <-playerNames)
      players = players :+ Player(player)
    for(npc <-npcNames)
      players = players :+ Player(npc,isNpc = true)
    players
  }

  def buyStreet(player: Player,streetNr: Int): Player ={
    val updated = player.buyStreet(streetNr)
    updated
  }

  def checkHypothek(): Unit = {
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
                            checkDept(-1)
                            notifyObservers(playerPaysHyptohekOnStreetEvent(players(isturn), board(i).asInstanceOf[Street]))
                        }
                    }
                case s: Trainstation =>
                    if (board(i).asInstanceOf[Trainstation].owner == isturn) {
                        // wenn er die hypothek zahlen kann tut er dies
                        if (s.hypothek && players(isturn).money > s.price) {
                            board = board.updated(i, board(i).asInstanceOf[Trainstation].payHypothek())
                            players = players.updated(isturn, players(isturn).decMoney(s.price))
                            checkDept(-1)
                            notifyObservers(playerPaysHyptohekOnTrainstationEvent(players(isturn), board(i).asInstanceOf[Trainstation]))
                        }
                    }
                case _ =>
            }
        }
    }
    def checkDept(player: Player): Player = {
        // wenn spieler im minus its wird
        // so lange verkauft bis im plus oder nichts mehr verkauft wurde dann gameover

        if (player.money <= 0) {
            notifyObservers(playerHasDeptEvent(players(isturn)))
            var actionDone = false
            breakable { // break wenn player plus -> breakable from scala.util.
                do {
                    // uebers ganze spielfeld gehen todo über besitzliste von owner
                    for (i <- board.indices) {
                        actionDone = false
                        board(i) match {
                            // alle felder durchgehen und schauen ob spieler der besitzer sit
                            //todo straßen, karten, ... verkaufen, später an spieler oder bank
                            case s: Street =>
                                // erst auf hypothek setzen dann an bank verkaufen
                                if (board(i).asInstanceOf[Street].owner == isturn) {
                                    // todo hotels dann haeuser zuerst verkaufen
                                    // strasse mit hypothek belasten
                                    if (!s.mortgage) {
                                        board = board.updated(i, board(i).asInstanceOf[Street].getMortgage)
                                        players = players.updated(isturn, players(isturn).incMoney(s.price))
                                        notifyObservers(playerUsesHyptohekOnStreetEvent(players(isturn), board(i).asInstanceOf[Street]))
                                        actionDone = true
                                    } else {
                                        //sonst straße verkaufen an bank todo später an spieler
                                        board = board.updated(i, board(i).asInstanceOf[Street].setOwner(-1))
                                        players = players.updated(isturn, players(isturn).incMoney(s.price))
                                        notifyObservers(playerSellsStreetEvent(players(isturn), board(i).asInstanceOf[Street]))
                                        actionDone = true
                                    }
                                }
                            case s: Trainstation =>
                                if (board(i).asInstanceOf[Trainstation].owner == isturn) {
                                    // bahnhof mit hypothek belasten
                                    if (!s.hypothek) {
                                        board = board.updated(i, board(i).asInstanceOf[Trainstation].getHypothek())
                                        players = players.updated(isturn, players(isturn).incMoney(s.price))
                                        notifyObservers(playerUsesHyptohekOnTrainstationEvent(players(isturn), board(i).asInstanceOf[Trainstation]))
                                        actionDone = true
                                    } else {
                                        // an bank verkaufen
                                        board = board.updated(i, board(i).asInstanceOf[Trainstation].setOwner(-1))
                                        players = players.updated(isturn, players(isturn).incMoney(s.price))
                                        notifyObservers(playerSellsTrainstationEvent(players(isturn), board(i).asInstanceOf[Trainstation]))
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
                println(players)
                players.filterNot(o => o == players(isturn)) // spieler aus dem spiel nehmen
                println(players)
                playerCount -= 1 // playercount muss auch um 1 verringert werden
                notifyObservers(brokeEvent(players(isturn)))
            }
        }
    }

    def buyStreet(field: Street): Boolean = {
        if (players(isturn).money >= field.price) {
            players = players.updated(isturn, players(isturn).decMoney(field.price))
            // spieler.besitz add streetnr
            board = board.updated(players(isturn).position, field.setOwner(isturn))
        }
        notifyObservers(buyStreetEvent(players(isturn), field))
        true
    }

    def movePlayer(sumDiceThrow: Int): Unit = {
        // spieler bewegen
        players = players.updated(isturn, players(isturn).move(sumDiceThrow))
        // schauen ob über los gegangen
        if (players(isturn).position >= 40) {
            notifyObservers(playerWentOverGoEvent(players(isturn)))
            players = players.updated(isturn, players(isturn).incMoney(1000))
            players = players.updated(isturn, players(isturn).moveBack(40))
        }
        // neue position ausgeben
        notifyObservers(playerMoveEvent(players(isturn)))
        // aktion fuer betretetenes feld ausloesen
        val field = board(players(isturn).position)
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
        }
    }

    def payRent(from:Player,to:Player,rent: Int): (Player,Player) = {
        //miete abziehen
        var updatedPayer = from.decMoney(rent)
        val updatedBenifiter = to.incMoney(rent)
        // schauen ob player ins minus gekommen ist
        updatedPayer = checkDept(updatedPayer)
        (updatedPayer,updatedBenifiter)
    }


}
