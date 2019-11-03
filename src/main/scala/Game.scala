import model._

import io.StdIn._


class Game {
    //spielBrett erstellen
    val spielBrett: Array[Cell] = createSpielBrett
    var playerCount = 0
    var players: Array[Player] = getPlayers
    var amzug = 0 // aktueller spieler
    val dice = Dice() // todo
    var runde = 1

    def createSpielBrett: Array[Cell] = {
        val spielBrett = new Array[Cell](40)

        spielBrett(0) = Los("Los", 0)
        spielBrett(1) = Street("Strasse1", 1, 60, -1, 200, 0)
        spielBrett(2) = CommunityChest("Gemeinschaftsfeld1", 0)
        spielBrett(3) = Street("Strasse2", 1, 60, -1, 200, 0)
        spielBrett(4) = IncomeTax("Einkommensteuer", 0)
        spielBrett(5) = Trainstation("Suedbahnhof", 9, 200, -1, 200)
        spielBrett(6) = Street("Strasse3", 2, 100, -1, 200, 0)
        spielBrett(7) = Eventcell("Ereignisfeld1", 0)
        spielBrett(8) = Street("Strasse4", 2, 100, -1, 200, 0)
        spielBrett(9) = Street("Strasse5", 2, 120, -1, 200, 0)
        spielBrett(10) = JailVisit("Zu besuch im Gefaengnis", 0)

        spielBrett(11) = Street("Strasse6", 3, 140, -1, 200, 0)
        spielBrett(12) = Elektrizitaetswerk("Elektrizitaetswerk", 10, 150, -1, 200)
        spielBrett(13) = Street("Strasse7", 3, 140, -1, 200, 0)
        spielBrett(14) = Street("Strasse8", 3, 160, -1, 200, 0)
        spielBrett(15) = Trainstation("Westbahnhof", 9, 200, -1, 200)
        spielBrett(16) = Street("Strasse9", 4, 180, -1, 200, 0)
        spielBrett(17) = CommunityChest("Gemeinschaftsfeld2", 0)
        spielBrett(18) = Street("Strasse10", 4, 180, -1, 200, 0)
        spielBrett(19) = Street("Strasse11", 4, 200, -1, 200, 0)
        spielBrett(20) = FreiParken("Freiparken", 0)

        spielBrett(21) = Street("Strasse12", 5, 220, -1, 200, 0)
        spielBrett(22) = Eventcell("Ereignisfeld2", 0)
        spielBrett(23) = Street("Strasse13", 5, 220, -1, 200, 0)
        spielBrett(24) = Street("Strasse14", 5, 240, -1, 200, 0)
        spielBrett(25) = Trainstation("Nordbahnhof", 9, 200, -1, 200)
        spielBrett(26) = Street("Strasse15", 6, 260, -1, 500, 0)
        spielBrett(27) = Street("Strasse16", 6, 260, -1, 800, 0)
        spielBrett(28) = Wasserwerk("Wasserwerk", 10, 150, -1, 200)
        spielBrett(29) = Street("Strasse17", 6, 280, -1, 2500, 0)
        spielBrett(30) = GoToJail("Gehe ins Gefaengnis", 0)

        spielBrett(31) = Street("Strasse18", 7, 300, -1, 200, 0)
        spielBrett(32) = Street("Strasse19", 7, 300, -1, 200, 0)
        spielBrett(33) = CommunityChest("Gemeinschaftsfeld3", 0)
        spielBrett(34) = Street("Strasse20", 7, 320, -1, 200, 0)
        spielBrett(35) = Trainstation("Nordbahnhof", 9, 200, -1, 200)
        spielBrett(36) = Eventcell("Ereignisfeld3", 0)
        spielBrett(37) = Street("Strasse21", 8, 350, -1, 200, 0)
        spielBrett(38) = Zusatzsteuer("Zusatzsteuer", 0)
        spielBrett(39) = Street("Strasse22", 8, 400, -1, 200, 0)

        spielBrett
    }

    def getPlayers: Array[Player] = {
        // spieleranzahl und namen einlesen
        print("wie viele spieler? : ")
        playerCount = readInt()
        val feld = Array.ofDim[Player](playerCount)
        // spieler mit namen einlesensr
        for (i <- 0 until playerCount) {
            println("Enter name player" + (i + 1) + ":")
            feld(i) = Player(readLine())
        }
        // todo alle spieler dürfen für reihenfolge würfeln
        feld
    }

    def run: Unit = {
        do {
            println("\nRunde " + runde + " beginnt!\n")
            for (i <- 0 until playerCount) {
                amzug = i
                // jeder der noch geld hat darf seinen zug ausfuehren
                if (players(amzug).money > 0) {
                    println("\nSpieler: " + players(amzug).toString + " ist am zug!")
                    // schauen ob spieler frei oder im jail ist
                    if (players(amzug).jailCount > -1) playerInJail //todo kann man im jail traden?
                    else {
                        // Todo handeln, strassen verkaufen,hypothek bezahlen etc vor dem wuerfeln
                        normalerZug
                        // todo falls der spieler nicht pleite geht und nicht im jail landet darf er noch handeln
                    }
                    // zugende
                    //Thread.sleep(1000) // wait for 1000 millisecond between player moves
                }
            }
            // Rundenende
            println("\n\nRunde " + runde + " vorbei:")
            printPlayersAndStreets
            runde += 1
            Thread.sleep(1000) // wait for 1000 millisecond between rounds
        } while (!gameOver) // Spielende abfragen
        printWinner
    }

    def printPlayersAndStreets: Unit = {
        println("\nSpieler: ")
        for (player <- players) println(player.toString)
        println("\nStraßen: ")
        for (i <- spielBrett.indices) {
            spielBrett(i) match { // todo komplettes spielfeld iwi ausgeben
                case s: Street if s.owner != -1 => println(s.toString + " Owner: " + players(s.owner).name)
                case s: Trainstation if s.owner != -1 => println(s.toString + " Owner: " + players(s.owner).name)
                case s: Elektrizitaetswerk if s.owner != -1 => println(s.toString + " Owner: " + players(s.owner).name)
                case s: Wasserwerk if s.owner != -1 => println(s.toString + " Owner: " + players(s.owner).name)
                //case e: Eventcell => println(e.toString)
                case _ =>
            }
        }
    }

    def printWinner: Unit = {
        print("Spielende: ")
        for (player <- players)
            if (player.money > 0)
                print(player.name + " is the winner!")
    }

    def gameOver: Boolean = {
        var playersWithMoney = playerCount
        for (i <- 0 until playerCount) {
            print(players(i).money)
            if (players(i).money <= 0) playersWithMoney -= 1
        }
        if (playersWithMoney <= 1)
            return true
        false
    }

    def wuerfeln: (Int, Int, Boolean) = {
        print("wuerfeln: ")
        val diceThrow1 = dice.throwDice
        val diceThrow2 = dice.throwDice
        var pasch = false
        println("Gewuerfelt: " + diceThrow1 + " " + diceThrow2)
        // schauen ob pasch gewuerfelt wurde
        if (dice.checkPash(diceThrow1, diceThrow2)) pasch = true
        (diceThrow1, diceThrow2, pasch)
    }

    def normalerZug: Unit = {
        // init pasch
        var pasch = true
        var paschCount = 0
        // wuerfeln
        while (pasch) {
            val throwDices = wuerfeln // 1 = wurf1, 2 = wurf 2, 3 = pasch
            if (throwDices._3) paschCount += 1
            else pasch = false
            //3x pasch gleich jail sonst move player
            if (paschCount == 3) {
                println("3x Pash -> jail")
                players(amzug) = players(amzug).moveToJail
            } else movePlayer(throwDices._1 + throwDices._2)
        }
    }

    def playerInJail: Unit = {
        print("player is in jail")
        print("Jailcount: " + (players(amzug).jailCount + 1))
        print("options for jail: buyOut, useCard, rollDice")
        //var options = "buyOut useCard rollDice"

        val option = "rollDice" // todo options holen
        // Folgende optionen: man kann 200 zahlen...
        if (option == "buyOut") {
            players(amzug) = players(amzug).decMoney(200)
            checkMoney(-1) // owner = bank
            players(amzug) = players(amzug).resetJailCount
            print("player is free again")
            normalerZug
        }
        // ...die freikarte benutzen....
        if (option == "useCard") {
            print("player is free again")
            normalerZug
        }
        // ...oder pasch wuerfeln.
        if (option == "rollDice") {
            val throwDices = wuerfeln
            if (throwDices._3) {
                // bei gewuerfelten pasch kommt man raus und moved
                players(amzug) = players(amzug).resetJailCount
                print("player is free again")
                movePlayer(throwDices._1 + throwDices._2)
            } else {
                //sonst jailcount +1
                players(amzug) = players(amzug).incJailTime
                // wenn man 3 runden im jail ist kommt man raus, zahlt und moved
                if (players(amzug).jailCount == 3) {
                    players(amzug) = players(amzug).resetJailCount
                    players(amzug) = players(amzug).decMoney(200)
                    checkMoney(-1) // owner is bank
                    print("player is free again")
                    movePlayer(throwDices._1 + throwDices._2)
                } else print("player remains in jail")
            }
        }
    }

    def movePlayer(sumDiceThrow: Int): Unit = {
        // spieler bewegen
        players(amzug) = players(amzug).move(sumDiceThrow)
        println("new pos: " + players(amzug).position)
        // schauen ob über los gegangen
        if (players(amzug).position >= 40) {
            players(amzug) = players(amzug).incMoney(200)
            players(amzug) = players(amzug).moveBack(40)
        }
        // aktion fuer betretetenes feld ausloesen
        val field = spielBrett(players(amzug).position)
        field match {
            case e: Los => activateStart(field.asInstanceOf[Los])
            case e: Street => activateStreet(field.asInstanceOf[Street])
            case e: Trainstation => activateTrainstation(field.asInstanceOf[Trainstation])
            case e: Eventcell => activateEvent(field.asInstanceOf[Eventcell])
            case e: CommunityChest => activateCommunityChest(field.asInstanceOf[CommunityChest])
            case e: IncomeTax => activateIncomeTax(field.asInstanceOf[IncomeTax])
            case e: JailVisit => activateJailVisit(field.asInstanceOf[JailVisit])
            case e: Elektrizitaetswerk => activateElektrizitaetswerk(field.asInstanceOf[Elektrizitaetswerk])
            case e: Wasserwerk => activateWasserwerk(field.asInstanceOf[Wasserwerk])
            case e: Zusatzsteuer => activateZusatzsteuer(field.asInstanceOf[Zusatzsteuer])
            case e: FreiParken => activateFreiParken(field.asInstanceOf[FreiParken])
            case e: GoToJail => activateGoToJail(field.asInstanceOf[GoToJail])
            case e: Jail => activateJail(field.asInstanceOf[Jail])
            case _ =>
        }
    }

    // activate street

    def activateStreet(field: Street): Unit = {
        val option = field.onPlayerEntered(amzug)
        println("option: " + option)
        if (option == "buy") {
            // wer geld hat kauft die straße
            buyStreet(field)
            //ansonsten miete zahlen
        } else if (option == "pay") {
            payRent(field)
        } else if (option == "buy home") {
            buyHome(field)
        }
    }

    def buyStreet(field: Street): Unit = {
        if (players(amzug).money >= field.price) {
            println("buy street")
            players(amzug) = players(amzug).decMoney(field.price)
            spielBrett(players(amzug).position) = field.setOwner(amzug)
            println(players(amzug).money)
        } else {
            println("Can´t afford street")
        }
    }

    def buyHome(field: Street): Unit = {
        if (players(amzug).money > 200)
            players(amzug).decMoney(200)
        // todo if player owns group of streets buy house
        // todo if housecount = street.maxhouses buy hotel
        spielBrett(players(amzug).position) = spielBrett(players(amzug).position).asInstanceOf[Street].buyHome(1)
    }

    def payRent(field: Street): Unit = {
        // mietpreis holen
        val rent = field.rent
        println("pay rent: " + rent)
        //miete abziehen
        players(amzug) = players(amzug).decMoney(rent)
        players(field.owner) = players(field.owner).incMoney(rent)
        // schauen ob player ins minus gekommen ist
        checkMoney(field.owner)
    }

    def checkMoney(owner: Int): Unit = {
        if (players(amzug).money <= 0) {
            //todo          // hotels haeuser hypothek dann straßen verkaufen bis uber 0
            println("Spieler ist im minus: " + players(amzug).money)
            for (i <- spielBrett.indices) {
                spielBrett(i) match {
                    // besitz suchen und verkaufen bis über 0
                    //todo straßen, karten, ... verkaufen, später an spieler oder bank
                    case s: Street =>
                        // an bank verkaufen
                        if (spielBrett(i).asInstanceOf[Street].owner == amzug) {
                            spielBrett(i) = spielBrett(i).asInstanceOf[Street].setOwner(-1)
                            players(amzug) = players(amzug).incMoney(s.price)
                            println(players(amzug).name + " verkaufte: " + spielBrett(i).asInstanceOf[Street].name + " an die Bank")
                            println("neuer kontostand: " + players(amzug).money)
                        }
                    case s: Trainstation =>
                        if (spielBrett(i).asInstanceOf[Trainstation].owner == amzug) {
                            // an bank verkaufen
                            spielBrett(i) = spielBrett(i).asInstanceOf[Trainstation].setOwner(-1)
                            players(amzug) = players(amzug).incMoney(s.price)
                            println(players(amzug).name + " verkaufte: " + spielBrett(i).asInstanceOf[Trainstation].name + " an die Bank")
                            println("neuer kontostand: " + players(amzug).money)
                        }
                    case _ =>
                }
            }
            // wenn immernoch pleite dann game over oder todo "declare bankrupt" später
            if (players(amzug).money <= 0) {
                playerGameOver(owner)
            }
        }
    }

    def playerGameOver(owner: Int): Unit = {
        //Straßen an Besitzer oder bank abgeben (je nachdem wer owner ist)
        for (i <- spielBrett.indices) {
            spielBrett(i) match {
                //todo bahnhöfe, karten,
                case s: Street =>
                    if (spielBrett(i).asInstanceOf[Street].owner == amzug) {
                        spielBrett(i) = spielBrett(i).asInstanceOf[Street].setOwner(owner)
                        println(players(amzug).name + " gab: " + spielBrett(i).asInstanceOf[Street].name + " an " + owner)
                    }
                case s: Trainstation =>
                    if (spielBrett(i).asInstanceOf[Trainstation].owner == amzug) {
                        spielBrett(i) = spielBrett(i).asInstanceOf[Trainstation].setOwner(owner)
                        println(players(amzug).name + " gab: " + spielBrett(i).asInstanceOf[Street].name + " an " + owner)
                    }
                //case e:Eventcell =>
                case _ =>
            }
        }
        println(players(amzug).name + " ist pleite")
    }

    // todo activate the different fields

    def activateStart(field: Los): Unit = {
        field.onPlayerEntered(amzug)
        players(amzug) = players(amzug).incMoney(200)
    }

    def activateEvent(field: Eventcell): Unit = {
        field.onPlayerEntered(amzug)
    }

    def activateJail(field: Jail): Unit = {
        field.onPlayerEntered(amzug)
        players(amzug).moveToJail
    }

    def activateGoToJail(field: GoToJail): Unit = {
        field.onPlayerEntered(amzug)
    }

    def activateFreiParken(field: FreiParken): Unit = {
        field.onPlayerEntered(amzug)
    }

    def activateCommunityChest(field: CommunityChest): Unit = {
        field.onPlayerEntered(amzug)
    }

    def activateIncomeTax(field: IncomeTax): Unit = {
        field.onPlayerEntered(amzug)
    }

    def activateJailVisit(field: JailVisit): Unit = {
        field.onPlayerEntered(amzug)
    }

    def activateElektrizitaetswerk(field: Elektrizitaetswerk): Unit = {
        field.onPlayerEntered(amzug)
    }

    def activateTrainstation(field: Trainstation): Unit = {
        field.onPlayerEntered(amzug)
    }

    def activateWasserwerk(field: Wasserwerk): Unit = {
        field.onPlayerEntered(amzug)
    }

    def activateZusatzsteuer(field: Zusatzsteuer): Unit = {
        field.onPlayerEntered(amzug)
    }
}


object Game {
    def main(args: Array[String]): Unit = {
        new Game().run // Companion Class
    }

}