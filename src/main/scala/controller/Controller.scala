package controller

import model._
import util.Observable

import scala.util.control.Breaks._


//PlayerIsHumanorNpcStrategy.strategy


class Controller extends Observable {
    var board: Vector[Cell] = Vector[Cell]()
    var playerCount = 0
    var players: Vector[Player] = Vector[Player]()
    var isturn = 0 // aktueller spieler
    var round = 1
    val dice = Dice()


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


    def checkDept(owner: Int): Unit = {
        // wenn spieler im minus its wird
        // so lange verkauft bis im plus oder nichts mehr verkauft wurde dann gameover

        if (players(isturn).money <= 0) {
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
                notifyObservers(brokeEvent(players(isturn)))
            }
        }
    }

    def wuerfeln: (Int, Int, Boolean) = {
        val roll1 = dice.roll
        val roll2 = dice.roll
        var pasch = false
        if (dice.checkPash(roll1, roll2)) {
            pasch = true
        }
        (roll1, roll2, pasch)
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
            case e: Los => activateStart(e.asInstanceOf[Los])
            case e: Street => activateStreet(e.asInstanceOf[Street])
            case e: Trainstation => activateTrainstation(e.asInstanceOf[Trainstation])
            case e: Eventcell => activateEvent(e.asInstanceOf[Eventcell])
            case e: CommunityChest => activateCommunityChest(e.asInstanceOf[CommunityChest])
            case e: IncomeTax => activateIncomeTax(e.asInstanceOf[IncomeTax])
            case e: Elektrizitaetswerk => activateElektrizitaetswerk(e.asInstanceOf[Elektrizitaetswerk])
            case e: Wasserwerk => activateWasserwerk(e.asInstanceOf[Wasserwerk])
            case e: Zusatzsteuer => activateZusatzsteuer(e.asInstanceOf[Zusatzsteuer])
            case e: FreiParken => activateFreiParken(e.asInstanceOf[FreiParken])
            case e: GoToJail => activateGoToJail(e.asInstanceOf[GoToJail])
            case e: Jail => activateJail(e.asInstanceOf[Jail])
            case _ =>
        }
    }

    def payRent(field: Street): Unit = {
        // mietpreis holen
        val rent = field.rent
        //miete abziehen
        players = players.updated(isturn, players(isturn).decMoney(rent))
        players = players.updated(field.owner, players(field.owner).incMoney(rent))
        notifyObservers(payRentEvent(players(isturn), players(field.owner)))
        // schauen ob player ins minus gekommen ist
        checkDept(field.owner)
    }

    def activateStreet(field: Street): Unit = {
        val option = field.onPlayerEntered(isturn)
        notifyObservers(optionEvent(option))

        if (option == "buy") {
            // wer geld hat kauft die straße
            buyStreet(field)
            //ansonsten miete zahlen falls keine hypothek
        } else if (option == "pay") {
            if (!field.mortgage) payRent(field)
            else notifyObservers(streetOnHypothekEvent(field))

        } else if (option == "buy home") {
            buyHome(field)
        }
    }

    def activateStart(field: Los): Unit = {
        field.onPlayerEntered(isturn)
        players = players.updated(isturn, players(isturn).incMoney(1000))
    }

    def buyHome(field: Street): Unit = {
        if (players(isturn).money > 200 && !field.mortgage) // nur wenn straße nicht hypothek hat
            players = players.updated(isturn, players(isturn).decMoney(200))
        // todo if player owns group of streets buy house
        // todo if housecount = street.maxhouses buy hotel
        board = board.updated(players(isturn).position, board(players(isturn).position).asInstanceOf[Street].buyHome(1))
    }

    def activateEvent(field: Eventcell): Unit = {
        field.onPlayerEntered(isturn)
    }

    def activateJail(field: Jail): Unit = {
        field.onPlayerEntered(isturn)
        players = players.updated(isturn, players(isturn).moveToJail)
    }

    def activateGoToJail(field: GoToJail): Unit = {
        field.onPlayerEntered(isturn)
    }

    def activateFreiParken(field: FreiParken): Unit = {
        field.onPlayerEntered(isturn)
    }

    def activateCommunityChest(field: CommunityChest): Unit = {
        field.onPlayerEntered(isturn)
    }

    def activateIncomeTax(field: IncomeTax): Unit = {
        field.onPlayerEntered(isturn)
    }

    def activateElektrizitaetswerk(field: Elektrizitaetswerk): Unit = {
        field.onPlayerEntered(isturn)
    }

    def activateTrainstation(field: Trainstation): Unit = {
        field.onPlayerEntered(isturn)
    }

    def activateWasserwerk(field: Wasserwerk): Unit = {
        field.onPlayerEntered(isturn)
    }

    def activateZusatzsteuer(field: Zusatzsteuer): Unit = {
        field.onPlayerEntered(isturn)
    }
}