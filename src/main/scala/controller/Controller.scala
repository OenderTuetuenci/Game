package controller

import model._
import util.Observable

import scala.util.control.Breaks._

class Controller extends Observable {
    var spielBrett: Vector[Cell] = createSpielBrett
    var playerCount = 0
    var players: Vector[Player] = Vector[Player]()
    var isturn = 0 // aktueller spieler
    var round = 1

    def createSpielBrett: Vector[Cell] = {
        var spielBrett = Vector[Cell]()
        spielBrett = spielBrett :+ Los("Los")
        spielBrett = spielBrett :+ Street("Strasse1", 1, 60, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ CommunityChest("Gemeinschaftsfeld1")
        spielBrett = spielBrett :+ Street("Strasse2", 1, 60, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ IncomeTax("Einkommensteuer")
        spielBrett = spielBrett :+ Trainstation("Suedbahnhof", 9, 200, -1, 200, hypothek = false)
        spielBrett = spielBrett :+ Street("Strasse3", 2, 100, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ Eventcell("Ereignisfeld1")
        spielBrett = spielBrett :+ Street("Strasse4", 2, 100, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ Street("Strasse5", 2, 120, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ Jail("Zu besuch oder im Gefaengnis")

        spielBrett = spielBrett :+ Street("Strasse6", 3, 140, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ Elektrizitaetswerk("Elektrizitaetswerk", 10, 150, -1, 200, hypothek = false)
        spielBrett = spielBrett :+ Street("Strasse7", 3, 140, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ Street("Strasse8", 3, 160, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ Trainstation("Westbahnhof", 9, 200, -1, 200, hypothek = false)
        spielBrett = spielBrett :+ Street("Strasse9", 4, 180, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ CommunityChest("Gemeinschaftsfeld2")
        spielBrett = spielBrett :+ Street("Strasse10", 4, 180, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ Street("Strasse11", 4, 200, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ FreiParken("Freiparken")

        spielBrett = spielBrett :+ Street("Strasse12", 5, 220, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ Eventcell("Ereignisfeld2")
        spielBrett = spielBrett :+ Street("Strasse13", 5, 220, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ Street("Strasse14", 5, 240, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ Trainstation("Nordbahnhof", 9, 200, -1, 200, hypothek = false)
        spielBrett = spielBrett :+ Street("Strasse15", 6, 260, -1, 500, 0, hypothek = false)
        spielBrett = spielBrett :+ Street("Strasse16", 6, 260, -1, 800, 0, hypothek = false)
        spielBrett = spielBrett :+ Wasserwerk("Wasserwerk", 10, 150, -1, 200, hypothek = false)
        spielBrett = spielBrett :+ Street("Strasse17", 6, 280, -1, 2500, 0, hypothek = false)
        spielBrett = spielBrett :+ GoToJail("Gehe ins Gefaengnis")

        spielBrett = spielBrett :+ Street("Strasse18", 7, 300, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ Street("Strasse19", 7, 300, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ CommunityChest("Gemeinschaftsfeld3")
        spielBrett = spielBrett :+ Street("Strasse20", 7, 320, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ Trainstation("Nordbahnhof", 9, 200, -1, 200, hypothek = false)
        spielBrett = spielBrett :+ Eventcell("Ereignisfeld3")
        spielBrett = spielBrett :+ Street("Strasse21", 8, 350, -1, 200, 0, hypothek = false)
        spielBrett = spielBrett :+ Zusatzsteuer("Zusatzsteuer")
        spielBrett = spielBrett :+ Street("Strasse22", 8, 400, -1, 200, 0, hypothek = false)

        spielBrett
    }

    def createPlayers(playerCount: Int, playerNames: Array[String]): Unit = {
        for (i <- 0 until playerCount) players = players :+ Player(playerNames(i))
        this.playerCount = playerCount
    }

    def runRound(): Unit = {
        notifyObservers(newRoundEvent(round))
        for (i <- 0 until playerCount) {
            isturn = i
            // jeder der noch geld hat darf seinen zug ausfuehren
            if (players(isturn).money > 0) {
                // Todo handeln, strassen verkaufen,hypothek bezahlen etc vor dem wuerfeln
                // schauen ob spieler frei oder im jail ist
                if (players(isturn).jailCount > -1) {
                    notifyObservers(playerInJailEvent(players(isturn)))
                    checkHypothek() // schauen ob haeuser im besitz hypotheken haben und bezahlen wenns geht
                    playerInJail()
                }
                else {
                    notifyObservers(normalTurnEvent(players(isturn)))
                    checkHypothek() // schauen ob haeuser im besitz hypotheken haben und bezahlen wenns geht
                    normalerZug()
                }
                // todo falls der spieler nicht pleite geht darf er handeln
                // zugende
                //Thread.sleep(1000) // wait for 1000 millisecond between player moves
            }
        }
        // Rundenende
        notifyObservers(endRoundEvent(round))
        notifyObservers(printEverythingEvent())
        round += 1
        //Thread.sleep(1000) // wait for 1000 millisecond between rounds
    }

    def checkHypothek(): Unit = {
        // in allen straßen des spielers suchen ob hypothek vorliegt
        // todo lieber feld von besitz beim spieler anlegen damit man nicht durch alle felder muss
        for (i <- spielBrett.indices) {
            spielBrett(i) match {
                case s: Street =>
                    if (spielBrett(i).asInstanceOf[Street].owner == isturn) {
                        // wenn er die hypothek zahlen kann tut er dies
                        if (s.hypothek && players(isturn).money > s.price) {
                            spielBrett = spielBrett.updated(i, spielBrett(i).asInstanceOf[Street].payHypothek())
                            players = players.updated(isturn, players(isturn).decMoney(s.price))
                            checkDept(-1)
                            notifyObservers(playerPaysHyptohekOnStreetEvent(players(isturn), spielBrett(i).asInstanceOf[Street]))
                        }
                    }
                case s: Trainstation =>
                    if (spielBrett(i).asInstanceOf[Trainstation].owner == isturn) {
                        // wenn er die hypothek zahlen kann tut er dies
                        if (s.hypothek && players(isturn).money > s.price) {
                            spielBrett = spielBrett.updated(i, spielBrett(i).asInstanceOf[Trainstation].payHypothek())
                            players = players.updated(isturn, players(isturn).decMoney(s.price))
                            checkDept(-1)
                            notifyObservers(playerPaysHyptohekOnTrainstationEvent(players(isturn), spielBrett(i).asInstanceOf[Trainstation]))
                        }
                    }
                case _ =>
            }
        }
    }

    def normalerZug(): Unit = {
        // init pasch
        var pasch = true
        var paschCount = 0
        // wuerfeln
        while (pasch && players(isturn).money > 0) {
            val throwDices = wuerfeln // 1 = wurf1, 2 = wurf 2, 3 = pasch
            notifyObservers(diceEvent(throwDices._1, throwDices._2, throwDices._3))
            if (throwDices._3) paschCount += 1
            else pasch = false
            //3x pasch gleich jail sonst move player
            if (paschCount == 3) {
                players = players.updated(isturn, players(isturn).moveToJail)
                notifyObservers(playerMoveToJail(players(isturn)))
            } else movePlayer(throwDices._1 + throwDices._2)
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
                    for (i <- spielBrett.indices) {
                        actionDone = false
                        spielBrett(i) match {
                            // alle felder durchgehen und schauen ob spieler der besitzer sit
                            //todo straßen, karten, ... verkaufen, später an spieler oder bank
                            case s: Street =>
                                // erst auf hypothek setzen dann an bank verkaufen
                                if (spielBrett(i).asInstanceOf[Street].owner == isturn) {
                                    // todo hotels dann haeuser zuerst verkaufen
                                    // strasse mit hypothek belasten
                                    if (!s.hypothek) {
                                        spielBrett = spielBrett.updated(i, spielBrett(i).asInstanceOf[Street].getHypothek())
                                        players = players.updated(isturn, players(isturn).incMoney(s.price))
                                        notifyObservers(playerUsesHyptohekOnStreetEvent(players(isturn), spielBrett(i).asInstanceOf[Street]))
                                        actionDone = true
                                    } else {
                                        //sonst straße verkaufen an bank todo später an spieler
                                        spielBrett = spielBrett.updated(i, spielBrett(i).asInstanceOf[Street].setOwner(-1))
                                        players = players.updated(isturn, players(isturn).incMoney(s.price))
                                        notifyObservers(playerSellsStreetEvent(players(isturn), spielBrett(i).asInstanceOf[Street]))
                                        actionDone = true
                                    }
                                }
                            case s: Trainstation =>
                                if (spielBrett(i).asInstanceOf[Trainstation].owner == isturn) {
                                    // bahnhof mit hypothek belasten
                                    if (!s.hypothek) {
                                        spielBrett = spielBrett.updated(i, spielBrett(i).asInstanceOf[Trainstation].getHypothek())
                                        players = players.updated(isturn, players(isturn).incMoney(s.price))
                                        notifyObservers(playerUsesHyptohekOnTrainstationEvent(players(isturn), spielBrett(i).asInstanceOf[Trainstation]))
                                        actionDone = true
                                    } else {
                                        // an bank verkaufen
                                        spielBrett = spielBrett.updated(i, spielBrett(i).asInstanceOf[Trainstation].setOwner(-1))
                                        players = players.updated(isturn, players(isturn).incMoney(s.price))
                                        notifyObservers(playerSellsTrainstationEvent(players(isturn), spielBrett(i).asInstanceOf[Trainstation]))
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
        val dice = Dice().roll
        val dice2 = Dice().roll
        var pasch = false
        if (dice.checkPash(dice.eyeCount, dice2.eyeCount)) {
            pasch = true
        }
        (dice.eyeCount, dice2.eyeCount, pasch)
    }

    def playerInJail(): Unit = {
        val option = "rollDice"
        if (option == "buyOut") {
            players = players.updated(isturn, players(isturn).decMoney(200))
            checkDept(-1) // owner = bank
            players = players.updated(isturn, players(isturn).resetJailCount)
            notifyObservers(playerIsFreeEvent(players(isturn)))
            normalerZug()
        }
        // ...die freikarte benutzen....
        if (option == "useCard") {
            notifyObservers(playerIsFreeEvent(players(isturn)))
            normalerZug()
        }
        // ...oder pasch wuerfeln.
        if (option == "rollDice") {
            val throwDices = wuerfeln
            if (throwDices._3) {
                // bei gewuerfelten pasch kommt man raus und moved
                players = players.updated(isturn, players(isturn).resetJailCount)
                notifyObservers(playerIsFreeEvent(players(isturn)))
                movePlayer(throwDices._1 + throwDices._2)
            } else {
                //sonst jailcount +1
                players = players.updated(isturn, players(isturn).incJailTime)
                // wenn man 3 runden im jail ist kommt man raus, zahlt und moved
                if (players(isturn).jailCount == 3) {
                    players = players.updated(isturn, players(isturn).resetJailCount)
                    players = players.updated(isturn, players(isturn).decMoney(200))
                    checkDept(-1) // owner is bank
                    notifyObservers(playerIsFreeEvent(players(isturn)))
                    movePlayer(throwDices._1 + throwDices._2)
                } else notifyObservers(playerRemainsInJailEvent(players(isturn)))
            }
        }
    }

    def getWinner: Player = {
        var winner: Player = players(0) //todo find better solution
        for (player <- players) {
            if (player.money > 0)
                winner = player
        }
        winner
    }

    def gameOver: Boolean = {
        var playersWithMoney = playerCount
        for (i <- 0 until playerCount) {
            if (players(i).money <= 0) {
                playersWithMoney -= 1
            }

        }
        if (playersWithMoney <= 1) {
            val winner = getWinner
            notifyObservers(gameOverEvent(winner))
            return true
        }
        false
    }

    def buyStreet(field: Street): Unit = {
        if (players(isturn).money >= field.price) {
            players = players.updated(isturn, players(isturn).decMoney(field.price))
            spielBrett = spielBrett.updated(players(isturn).position, field.setOwner(isturn))
        }
        notifyObservers(buyStreetEvent(players(isturn), field))
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
        val field = spielBrett(players(isturn).position)
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
            if (!field.hypothek) payRent(field)
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
        if (players(isturn).money > 200 && !field.hypothek) // nur wenn straße nicht hypothek hat
            players = players.updated(isturn, players(isturn).decMoney(200))
        // todo if player owns group of streets buy house
        // todo if housecount = street.maxhouses buy hotel
        spielBrett = spielBrett.updated(players(isturn).position, spielBrett(players(isturn).position).asInstanceOf[Street].buyHome(1))
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

    // Strings für die tui

    def getPlayerAndBoardToString: String = {
        var string = ""
        string += "\nSpieler: "
        for (player <- players) string += player.toString + "\n"
        string += "\nSpielfeld:\n"
        for (i <- spielBrett.indices) {
            // spieler die noch in der runde sind raussuchen
            var playersOnThisField = ""
            for (player <- players) {
                if (player.money > 0 && player.position == i) {
                    playersOnThisField += player.name + " "
                }
            }
            // felder anzeigen
            spielBrett(i) match {
                case s: Los => string += s.toString
                case s: Eventcell => string += s.toString
                case s: CommunityChest => string += s.toString
                case s: IncomeTax => string += s.toString
                case s: Jail => string += s.toString
                case s: Elektrizitaetswerk => string += s.toString
                case s: Wasserwerk => string += s.toString
                case s: Zusatzsteuer => string += s.toString
                case s: FreiParken => string += s.toString
                case s: GoToJail => string += s.toString
                case s: Street =>
                    // besitzer des feldes suchen
                    var owner = s.owner.toString
                    if (s.owner != -1) owner = players(s.owner).name
                    string += s.toString + " | Owner: " + owner
                case s: Trainstation =>
                    // besitzer des feldes suchen
                    var owner = s.owner.toString
                    if (s.owner != -1) owner = players(s.owner).name
                    string += s.toString + " | Owner: " + owner
                case s: Elektrizitaetswerk =>
                    // besitzer des feldes suchen
                    var owner = s.owner.toString
                    if (s.owner != -1) owner = players(s.owner).name
                    string += s.toString + " | Owner: " + owner
                case s: Wasserwerk =>
                    // besitzer des feldes suchen
                    var owner = s.owner.toString
                    if (s.owner != -1) owner = players(s.owner).name
                    string += s.toString + " | Owner: " + owner
            }
            // spieler die sich auf dem aktuellen feld befinden werden angezeigt
            if (playersOnThisField != "") string += " | players on this field: " + playersOnThisField
            string += "\n"
        }
        string
    }

    def getRollString(e: diceEvent): String = {
        var string = "throwing Dice:\n"
        string += "rolled :" + e.eyeCount1 + " " + e.eyeCount2 + "\n"
        if (e.pasch)
            string += "rolled pasch!"
        string
    }

    def getNormalTurnString(e: normalTurnEvent): String = {
        val string = "Its " + e.player.toString + " turn!\n"
        string
    }

    def getPlayerInJailString(e: playerInJailEvent): String = {
        var string = "\nIts " + e.player.toString + " turn. he is in jail!\n"
        string += "Jailcount: " + (e.player.jailCount + 1) + "\n"
        string
    }

    def getPlayerIsFreeString(e: playerIsFreeEvent): String = e.player.name + " is free again!"


    def getPlayerRemainsInJailString(e: playerRemainsInJailEvent): String = e.player.name + " remains in jail"


    def getStreetOnHypothekString(e: streetOnHypothekEvent): String = e.street.name + " is on hypothek. "

    def getBuyStreetEventString(e: buyStreetEvent): String = {
        var string = e.player.money + "\n"
        if (e.player.money > e.street.price)
            string = "bought " + e.street.name
        else
            string = "can´t afford street"
        string
    }

    def getBuyTrainstationEventString(e: buyTrainstationEvent): String = {
        var string = e.player.money + "\n"
        if (e.player.money > e.street.price)
            string += "bought " + e.street.name
        else
            string += "can´t afford street"
        string
    }

    def getPayRentString(e: payRentEvent): String = e.from.name + " pays rent to " + e.to.name

    def getPlayerWentOverGoString(e: playerWentOverGoEvent): String = e.player.name + " went over go."

    def getPlayerWentOnGoString(e: playerWentOnGoEvent): String = e.player.name + " went on go and gets extra money."

    def getGameOverString(e: gameOverEvent): String = e.winner.name + " is the winner!!"

    def getBrokeEventString(e: brokeEvent): String = e.player.name + " is broke!!"

    def getPlayerUsesHypothekOnStreetString(e: playerUsesHyptohekOnStreetEvent): String = {
        e.player.name + " gets Hypothek for " + e.street.name + " new creditbalance: " + e.player.money

    }

    def getPlayerPaysHypothekOnStreetString(e: playerPaysHyptohekOnStreetEvent): String = {
        e.player.name + " pays Hypothek for " + e.street.name + " new creditbalance: " + e.player.money
    }

    def getPlayerSellsStreetString(e: playerSellsStreetEvent): String = {
        var string = e.from.name + " sells " + e.street.name + "\n"
        string += "new creditbalance: " + e.from.money
        string
    }

    def getPlayerUsesHypothekOnTrainstationString(e: playerUsesHyptohekOnTrainstationEvent): String = {
        e.player.name + " pays Hypothek for " + e.street.name + " new creditbalance: " + e.player.money
    }

    def getPlayerPaysHypothekOnTrainstationString(e: playerPaysHyptohekOnTrainstationEvent): String = {
        e.player.name + " pays Hypothek for " + e.street.name + " new creditbalance: " + e.player.money
    }

    def getPlayerSellsTrainstationString(e: playerSellsTrainstationEvent): String = {
        var string = e.from.name + " sells " + e.street.name + "\n"
        string += "new creditbalance: " + e.from.money
        string
    }

    def getEndRoundString(e: endRoundEvent): String = "\n\n\nround " + e.round + " ends"

    def getNewRoundString(e: newRoundEvent): String = "\n\nround " + e.round + " starts"

    def getPlayerMoveToJailString(e: playerMoveToJail): String = e.player.name + " moved to Jail!"

    def getPlayerMovedString(e: playerMoveEvent): String = e.player.name + " moved to " + e.player.position

    def getPlayerHasDeptEventString(e: playerHasDeptEvent): String = e.player.name + " " + " is in minus: " + e.player.money
}