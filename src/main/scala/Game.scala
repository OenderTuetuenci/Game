import model._

import io.StdIn._


class Gamelogic {
  //spielBrett erstellen
  val spielBrett: Array[Cell] = createSpielBrett
  var playerCount = 0
  var players: Array[Player] = getPlayers
  val dice = Dice()
  var runde = 1
  var amzug = 0
  var streetNr = 0

  def createSpielBrett: Array[Cell] = {
    val spielBrett = new Array[Cell](40)

    spielBrett(0) = Los("Los", 0)
    spielBrett(1) = Street("Strasse1", 1, 60, -1, 200, 0)
    spielBrett(2) = CommunityChest("Gemeinschaftsfeld1", 2)
    spielBrett(3) = Street("Strasse2", 1, 60, -1, 200, 0)
    spielBrett(4) = IncomeTax("Einkommensteuer", 0)
    spielBrett(5) = SouthTrainstation("Suedbahnhof", 9, 200, -1, 200)
    spielBrett(6) = Street("Strasse3", 2, 100, -1, 200, 0)
    spielBrett(7) = Eventcell("Ereignisfeld1", 0)
    spielBrett(8) = Street("Strasse4", 2, 100, -1, 200, 0)
    spielBrett(9) = Street("Strasse5", 2, 120, -1, 200, 0)
    spielBrett(10) = JailVisit("Zu besuch im Gefaengnis", 0)

    spielBrett(11) = Street("Strasse6", 3, 140, -1, 200, 0)
    spielBrett(12) = Elektrizitaetswerk("Elektrizitaetswerk", 10, 150, -1, 200)
    spielBrett(13) = Street("Strasse7", 3, 140, -1, 200, 0)
    spielBrett(14) = Street("Strasse8", 3, 160, -1, 200, 0)
    spielBrett(15) = WestTrainstation("Westbahnhof", 9, 200, -1, 200)
    spielBrett(16) = Street("Strasse9", 4, 180, -1, 200, 0)
    spielBrett(17) = CommunityChest("Gemeinschaftsfeld2", 0)
    spielBrett(18) = Street("Strasse10", 4, 180, -1, 200, 0)
    spielBrett(19) = Street("Strasse11", 4, 200, -1, 200, 0)
    spielBrett(20) = FreiParken("Freiparken", 0)

    spielBrett(21) = Street("Strasse12", 5, 220, -1, 200, 0)
    spielBrett(22) = Eventcell("Ereignisfeld2", 0)
    spielBrett(23) = Street("Strasse13", 5, 220, -1, 200, 0)
    spielBrett(24) = Street("Strasse14", 5, 240, -1, 200, 0)
    spielBrett(25) = NorthTrainstation("Nordbahnhof", 9, 200, -1, 200)
    spielBrett(26) = Street("Strasse15", 6, 260, -1, 500, 0)
    spielBrett(27) = Street("Strasse16", 6, 260, -1, 800, 0)
    spielBrett(28) = Wasserwerk("Wasserwerk", 10, 150, -1, 200)
    spielBrett(29) = Street("Strasse17", 6, 280, -1, 2500, 0)
    spielBrett(30) = GoToJail("Gehe ins Gefaengnis", 0)

    spielBrett(31) = Street("Strasse18", 7, 300, -1, 200, 0)
    spielBrett(32) = Street("Strasse19", 7, 300, -1, 200, 0)
    spielBrett(33) = CommunityChest("Gemeinschaftsfeld3", 0)
    spielBrett(34) = Street("Strasse20", 7, 320, -1, 200, 0)
    spielBrett(35) = MainTrainstation("Nordbahnhof", 9, 200, -1, 200)
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
    // spieler mit namen einlesens
    for (i <- 0 until playerCount) {
      println("Enter name player" + (i + 1) + ":")
      feld(i) = Player(readLine())
    }
    feld
  }

  def run: Unit = {
    do {
      println("\nRunde " + runde + " beginnt!\n")
      for (i <- 0 until playerCount) {
        amzug = i
        // jeder der noch geld hat darf wuerfeln
        if (players(amzug).money > 0) wuerfeln
        // Rundenende
        println("\n\nRunde " + runde + " vorbei:")
        runde += 1
        // draw
        println("\nSpieler: ")
        for (player <- players) println(player.toString)
        println("\nStraßen: ")
        for (i <- spielBrett.indices) {
          spielBrett(i) match {
            case s: Street if s.owner != -1 => println(s.toString + " Owner: " + players(s.owner).name)
            //case e: Eventcell => println(e.toString)
            case _ =>
          }
        }
        Thread.sleep(1000) // wait for 1000 millisecond between rounds
      }
    }
    while (!gameOver)
    // print winner
    print("Spielende: ")
    for (player <- players)
      if (player.money > 0)
        print(player.name + " is the winner!")
  }

  def gameOver: Boolean = {
    // Spielende abfragen
    var playersWithMoney = playerCount
    for (i <- 0 until playerCount) {
      print(players(i).money)
      if (players(i).money <= 0) playersWithMoney -= 1
    }
    if (playersWithMoney <= 1)
      return true
    false
  }

  def wuerfeln: Unit = {
    // init pasch
    var pasch = true
    var paschCount = 0
    // wuerfeln
    while (pasch) {
      print("\nSpieler: " + players(amzug).toString + ") wuerfeln: ")
      val diceThrow1 = dice.throwDice
      val diceThrow2 = dice.throwDice
      val sumDiceThrow = diceThrow1 + diceThrow2
      println("Gewuerfelt: " + sumDiceThrow)
      // schauen ob pasch gewuerfelt wurde
      if (dice.checkPash(diceThrow1, diceThrow2)) {
        println("Pasch")
        paschCount += 1
      } else {
        pasch = false
      }
      //3x pasch gleich jail
      if (paschCount == 3) {
        println("3x Pash -> jail")
        players(amzug) = players(amzug).moveToJail
      }
      //todo if player in jail else move player
      if (false) {
        // player is in jail
        print("todo jail")
      } else {
        // move player
        players(amzug) = players(amzug).move(sumDiceThrow)
        println("new pos: " + players(amzug).position)
        streetNr = players(amzug).position
        val field = spielBrett(streetNr)
        spielBrett(players(amzug).position) match {
          case e: Los => activateStart(field.asInstanceOf[Los])
          case e: Street => activateStreet(field.asInstanceOf[Street])
          case e: Eventcell => activateEvent(field.asInstanceOf[Eventcell])
          case e: CommunityChest => activateCommunityChest(field.asInstanceOf[CommunityChest])
          case e: IncomeTax => activateIncomeTax(field.asInstanceOf[IncomeTax])
          case e: JailVisit => activateJailVisit(field.asInstanceOf[JailVisit])
          case e: Elektrizitaetswerk => activateElektrizitaetswerk(field.asInstanceOf[Elektrizitaetswerk])
          case e: SouthTrainstation => activateSouthTrainstation(field.asInstanceOf[SouthTrainstation])
          case e: WestTrainstation => activateWestTrainstation(field.asInstanceOf[WestTrainstation])
          case e: NorthTrainstation => activateNorthTrainstation(field.asInstanceOf[NorthTrainstation])
          case e: MainTrainstation => activateMainTrainstation(field.asInstanceOf[MainTrainstation])
          case e: Wasserwerk => activateWasserwerk(field.asInstanceOf[Wasserwerk])
          case e: Zusatzsteuer => activateZusatzsteuer(field.asInstanceOf[Zusatzsteuer])
          case e: FreiParken => activateFreiParken(field.asInstanceOf[FreiParken])
          case e: GoToJail => activateGoToJail(field.asInstanceOf[GoToJail])
          case e: Jail => activateJail(field.asInstanceOf[Jail])
          case _ =>
        }
      }
      // zugende
      //Thread.sleep(1000) // wait for 1000 millisecond between player moves
    }
  }

  def activateStreet(field :Street): Unit = {

    val option = field.onPlayerEntered(amzug)
    println("option: " + option)
    if (option == "buy") {
      // wer geld hat kauft die straße
      if (players(amzug).money >= field.price) {
        println("buy street")
        players(amzug) = players(amzug).decMoney(field.price)
        spielBrett(streetNr) = field.setOwner(amzug)
        println(players(amzug).money)
      } else {
        println("Can´t afford street")
      }
      //ansonsten miete zahlen
    } else if (option == "pay") {
      // mietpreis holen
      val rent = field.rent
      println("pay rent: " + rent)
      //miete abziehen
      players(amzug) = players(amzug).decMoney(rent)
      players(field.owner) = players(field.owner).incMoney(rent)

      if (players(amzug).money <= 0) {
        //todo check hypotheken oder verkaufe
        //todo else straßen abgebe// todo array of any unterklasse of feld ??n
        //Straßen an Besitzer abgeben
        for (k <- spielBrett.indices) {
          spielBrett(k) match {
            case s: Street => spielBrett(k) = spielBrett(k).asInstanceOf[Street].setOwner(field.owner)
            //case e:Eventcell =>
            case _ =>
          }
        }
        println(players(amzug).name + " ist pleite")
      }
    } else if (option == "buy home") {
      if (players(amzug).money > 200)
        players(amzug).decMoney(200)
      spielBrett(streetNr) = spielBrett(streetNr).asInstanceOf[Street].buyHome(1)
    }
  }

  def activateStart(field :Los): Unit = {
    field.onPlayerEntered(amzug)
  }

  def activateEvent(field :Eventcell): Unit = {
    field.onPlayerEntered(amzug)
  }

  def activateJail(field :Jail): Unit = {
    field.onPlayerEntered(amzug)
  }

  def activateGoToJail(field :GoToJail): Unit = {
    field.onPlayerEntered(amzug)
  }

  def activateFreiParken(field :FreiParken): Unit = {
    field.onPlayerEntered(amzug)
  }

  def activateCommunityChest(field :CommunityChest): Unit = {
    field.onPlayerEntered(amzug)
  }
  def activateIncomeTax(field :IncomeTax): Unit = {
    field.onPlayerEntered(amzug)
  }
  def activateJailVisit(field :JailVisit): Unit = {
    field.onPlayerEntered(amzug)
  }
  def activateElektrizitaetswerk(field :Elektrizitaetswerk): Unit = {
    field.onPlayerEntered(amzug)
  }
  def activateSouthTrainstation(field :SouthTrainstation): Unit = {
    field.onPlayerEntered(amzug)
  }
  def activateWestTrainstation(field :WestTrainstation): Unit = {
    field.onPlayerEntered(amzug)
  }
  def activateNorthTrainstation(field :NorthTrainstation): Unit = {
    field.onPlayerEntered(amzug)
  }
  def activateMainTrainstation(field :MainTrainstation): Unit = {
    field.onPlayerEntered(amzug)
  }
  def activateWasserwerk(field :Wasserwerk): Unit = {
    field.onPlayerEntered(amzug)
  }
  def activateZusatzsteuer(field :Zusatzsteuer): Unit = {
    field.onPlayerEntered(amzug)
  }




}


object Game {
  def main(args: Array[String]): Unit = {
    new Gamelogic().run // Companion Class
  }

}