package controller

import model.{Cell, CommunityChest, Dice, Elektrizitaetswerk, Event, Eventcell, FreiParken, GameOverEvent, GoToJail, IncomeTax, Jail, Los, Player, Street, Trainstation, Wasserwerk, Zusatzsteuer, brokeEvent, buyStreetEvent, diceEvent, endRoundEvent, newRoundEvent, normalTurnEvent, optionEvent, payRentEvent, playerInJailEvent, playerIsFreeEvent, playerMoveEvent, playerMoveToJail, playerRemainsInJail, playerSellsStreetEvent, printEverythingEvent}
import util.Observable

class Controller extends Observable {
  var spielBrett: Array[Cell] = createSpielBrett
  var playerCount = 0
  var players:Array[Player] = _
  var isturn = 0 // aktueller spieler
  var round = 1

  def createSpielBrett: Array[Cell] = {
    val spielBrett = new Array[Cell](40)
    spielBrett(0) = Los("Los")
    spielBrett(1) = Street("Strasse1", 1, 60, -1, 200, 0)
    spielBrett(2) = CommunityChest("Gemeinschaftsfeld1")
    spielBrett(3) = Street("Strasse2", 1, 60, -1, 200, 0)
    spielBrett(4) = IncomeTax("Einkommensteuer")
    spielBrett(5) = Trainstation("Suedbahnhof", 9, 200, -1, 200)
    spielBrett(6) = Street("Strasse3", 2, 100, -1, 200, 0)
    spielBrett(7) = Eventcell("Ereignisfeld1")
    spielBrett(8) = Street("Strasse4", 2, 100, -1, 200, 0)
    spielBrett(9) = Street("Strasse5", 2, 120, -1, 200, 0)
    spielBrett(10) = Jail("Zu besuch oder im Gefaengnis")

    spielBrett(11) = Street("Strasse6", 3, 140, -1, 200, 0)
    spielBrett(12) = Elektrizitaetswerk("Elektrizitaetswerk", 10, 150, -1, 200)
    spielBrett(13) = Street("Strasse7", 3, 140, -1, 200, 0)
    spielBrett(14) = Street("Strasse8", 3, 160, -1, 200, 0)
    spielBrett(15) = Trainstation("Westbahnhof", 9, 200, -1, 200)
    spielBrett(16) = Street("Strasse9", 4, 180, -1, 200, 0)
    spielBrett(17) = CommunityChest("Gemeinschaftsfeld2")
    spielBrett(18) = Street("Strasse10", 4, 180, -1, 200, 0)
    spielBrett(19) = Street("Strasse11", 4, 200, -1, 200, 0)
    spielBrett(20) = FreiParken("Freiparken")

    spielBrett(21) = Street("Strasse12", 5, 220, -1, 200, 0)
    spielBrett(22) = Eventcell("Ereignisfeld2")
    spielBrett(23) = Street("Strasse13", 5, 220, -1, 200, 0)
    spielBrett(24) = Street("Strasse14", 5, 240, -1, 200, 0)
    spielBrett(25) = Trainstation("Nordbahnhof", 9, 200, -1, 200)
    spielBrett(26) = Street("Strasse15", 6, 260, -1, 500, 0)
    spielBrett(27) = Street("Strasse16", 6, 260, -1, 800, 0)
    spielBrett(28) = Wasserwerk("Wasserwerk", 10, 150, -1, 200)
    spielBrett(29) = Street("Strasse17", 6, 280, -1, 2500, 0)
    spielBrett(30) = GoToJail("Gehe ins Gefaengnis")

    spielBrett(31) = Street("Strasse18", 7, 300, -1, 200, 0)
    spielBrett(32) = Street("Strasse19", 7, 300, -1, 200, 0)
    spielBrett(33) = CommunityChest("Gemeinschaftsfeld3")
    spielBrett(34) = Street("Strasse20", 7, 320, -1, 200, 0)
    spielBrett(35) = Trainstation("Nordbahnhof", 9, 200, -1, 200)
    spielBrett(36) = Eventcell("Ereignisfeld3")
    spielBrett(37) = Street("Strasse21", 8, 350, -1, 200, 0)
    spielBrett(38) = Zusatzsteuer("Zusatzsteuer")
    spielBrett(39) = Street("Strasse22", 8, 400, -1, 200, 0)

    spielBrett
  }
  def createPlayers(playerCount:Int,playerNames:Array[String]): Unit = {
    players = Array.ofDim[Player](playerCount)
    for (i <-players.indices){
      players(i) =Player(playerNames(i))
    }
    this.playerCount = playerCount
  }

  def getWinner:Player = {
    var winner:Player = players(0)//todo find better solution
    for (player <- players){
      if(player.money > 0)
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
      notifyObservers(GameOverEvent(winner))
      return true
    }
    false
  }

  def getPlayerAndBoardToString: String = {
    var string = ""
    string += "\nSpieler: "
    for (player <- players) string += player.toString+"\n"
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
        case _ =>
      }
      // spieler die sich auf dem aktuellen feld befinden werden angezeigt
      if (playersOnThisField != "") string += " | players on this field: " + playersOnThisField
      string += "\n"
    }
    string
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
  def movePlayer(sumDiceThrow: Int): Unit = {
    // spieler bewegen
    players(isturn) = players(isturn).move(sumDiceThrow)
    // schauen ob über los gegangen
    if (players(isturn).position >= 40) {
      players(isturn) = players(isturn).incMoney(200)
      players(isturn) = players(isturn).moveBack(40)
    }
    // neue position ausgeben
    notifyObservers(playerMoveEvent(players(isturn)))
    // aktion fuer betretetenes feld ausloesen
    val field = spielBrett(players(isturn).position)
    field match {
      case e: Los => activateStart(field.asInstanceOf[Los])
      case e: Street => activateStreet(field.asInstanceOf[Street])
      case e: Trainstation => activateTrainstation(field.asInstanceOf[Trainstation])
      case e: Eventcell => activateEvent(field.asInstanceOf[Eventcell])
      case e: CommunityChest => activateCommunityChest(field.asInstanceOf[CommunityChest])
      case e: IncomeTax => activateIncomeTax(field.asInstanceOf[IncomeTax])
      case e: Elektrizitaetswerk => activateElektrizitaetswerk(field.asInstanceOf[Elektrizitaetswerk])
      case e: Wasserwerk => activateWasserwerk(field.asInstanceOf[Wasserwerk])
      case e: Zusatzsteuer => activateZusatzsteuer(field.asInstanceOf[Zusatzsteuer])
      case e: FreiParken => activateFreiParken(field.asInstanceOf[FreiParken])
      case e: GoToJail => activateGoToJail(field.asInstanceOf[GoToJail])
      case e: Jail => activateJail(field.asInstanceOf[Jail])
      case _ =>
    }
  }
  def normalerZug: Unit = {
    // init pasch
    var pasch = true
    var paschCount = 0
    // wuerfeln
    while (pasch && players(isturn).money > 0) {
      val throwDices = wuerfeln // 1 = wurf1, 2 = wurf 2, 3 = pasch
      notifyObservers(diceEvent(throwDices._1,throwDices._2,throwDices._3))
      if (throwDices._3) paschCount += 1
      else pasch = false
      //3x pasch gleich jail sonst move player
      if (paschCount == 3) {
        players(isturn) = players(isturn).moveToJail
        notifyObservers(playerMoveToJail(players(isturn)))
      } else movePlayer(throwDices._1 + throwDices._2)
    }
  }

  def playerInJail(): Unit = {
    val option = "rollDice"
    if (option == "buyOut") {
      players(isturn) = players(isturn).decMoney(200)
      checkMoney(-1) // owner = bank
      players(isturn) = players(isturn).resetJailCount
      notifyObservers(playerIsFreeEvent(players(isturn)))
      normalerZug
    }
    // ...die freikarte benutzen....
    if (option == "useCard") {
      notifyObservers(playerIsFreeEvent(players(isturn)))
      normalerZug
    }
    // ...oder pasch wuerfeln.
    if (option == "rollDice") {
      val throwDices = wuerfeln
      if (throwDices._3) {
        // bei gewuerfelten pasch kommt man raus und moved
        players(isturn) = players(isturn).resetJailCount
        notifyObservers(playerIsFreeEvent(players(isturn)))
        movePlayer(throwDices._1 + throwDices._2)
      } else {
        //sonst jailcount +1
        players(isturn) = players(isturn).incJailTime
        // wenn man 3 runden im jail ist kommt man raus, zahlt und moved
        if (players(isturn).jailCount == 3) {
          players(isturn) = players(isturn).resetJailCount
          players(isturn) = players(isturn).decMoney(200)
          checkMoney(-1) // owner is bank
          notifyObservers(playerIsFreeEvent(players(isturn)))
          movePlayer(throwDices._1 + throwDices._2)
        } else notifyObservers(playerRemainsInJail(players(isturn)))
      }
    }
  }

  def runRound():Unit = {
      notifyObservers(newRoundEvent(round))
      for (i <- 0 until playerCount) {
        isturn = i
        // jeder der noch geld hat darf seinen zug ausfuehren
        if (players(isturn).money > 0) {
          notifyObservers(normalTurnEvent(players(isturn)))
          // schauen ob spieler frei oder im jail ist
          //todo kann man im jail traden?
          if (players(isturn).jailCount > -1)playerInJail()
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
      notifyObservers(endRoundEvent(round))
      notifyObservers(printEverythingEvent())
      round += 1
      //Thread.sleep(1000) // wait for 1000 millisecond between rounds
  }
  def buyStreet(field: Street): Unit = {
    if (players(isturn).money >= field.price) {
      players(isturn) = players(isturn).decMoney(field.price)
      spielBrett(players(isturn).position) = field.setOwner(isturn)
    }
    notifyObservers(buyStreetEvent(players(isturn),field))
  }
  def checkMoney(owner: Int): Unit = {
    if (players(isturn).money <= 0) {
      //todo          // hotels haeuser hypothek dann straßen verkaufen bis uber 0
      for (i <- spielBrett.indices) {
        spielBrett(i) match {
          // besitz suchen und verkaufen bis über 0
          //todo straßen, karten, ... verkaufen, später an spieler oder bank
          case s: Street =>
            // an bank verkaufen
            if (spielBrett(i).asInstanceOf[Street].owner == isturn) {
              spielBrett(i) = spielBrett(i).asInstanceOf[Street].setOwner(-1)
              players(isturn) = players(isturn).incMoney(s.price)
              notifyObservers(playerSellsStreetEvent(players(isturn),spielBrett(i).asInstanceOf[Street]))
            }
          case s: Trainstation =>
            if (spielBrett(i).asInstanceOf[Trainstation].owner == isturn) {
              // an bank verkaufen
              spielBrett(i) = spielBrett(i).asInstanceOf[Trainstation].setOwner(-1)
              players(isturn) = players(isturn).incMoney(s.price)
              notifyObservers(playerSellsStreetEvent(players(isturn),spielBrett(i).asInstanceOf[Street]))
            }
          case _ =>
        }
      }
      // wenn immernoch pleite dann game over oder todo "declare bankrupt" später
      if (players(isturn).money <= 0) {
        notifyObservers(brokeEvent(players(isturn)))
      }
    }
  }
  def payRent(field: Street): Unit = {
    // mietpreis holen
    val rent = field.rent
    //miete abziehen
    players(isturn) = players(isturn).decMoney(rent)
    players(field.owner) = players(field.owner).incMoney(rent)
    notifyObservers(payRentEvent(players(isturn),players(field.owner)))
    // schauen ob player ins minus gekommen ist
    checkMoney(field.owner)
  }
  def activateStreet(field: Street): Unit = {
    val option = field.onPlayerEntered(isturn)
    notifyObservers(optionEvent(option))
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
  def buyHome(field: Street): Unit = {
    if (players(isturn).money > 200)
      players(isturn).decMoney(200)
    // todo if player owns group of streets buy house
    // todo if housecount = street.maxhouses buy hotel
    spielBrett(players(isturn).position) = spielBrett(players(isturn).position).asInstanceOf[Street].buyHome(1)
  }
  def activateStart(field: Los): Unit = {
    field.onPlayerEntered(isturn)
    players(isturn) = players(isturn).incMoney(200)
  }

  def activateEvent(field: Eventcell): Unit = {
    field.onPlayerEntered(isturn)
  }

  def activateJail(field: Jail): Unit = {
    field.onPlayerEntered(isturn)
    players(isturn).moveToJail
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
  def getRollString(e: diceEvent): String = {
    var string = "throwing Dice:\n"
    string += "rolled :"+e.eyeCount1+" "+e.eyeCount2+"\n"
    if(e.pasch)
      string += "rolled pasch!"
    string
  }

  def getNormalTurnString(e: normalTurnEvent): String = {
    var string = "Its "+e.player.name+" turn!\n"
    string
  }

  def getPlayerInJailString(e: playerInJailEvent): String = {
    var string = e.player.name+"is in jail!\n"
    string += "Jailcount: "+e.player.jailCount+1+"\n"
    if(e.player.jailCount < 3)
      string += e.player.name+" remains in jail"
    else
      string += e.player.name+" is free again!"
    string
  }

  def getBuyStreetEventString(e: buyStreetEvent): String = {
    var string =e.player.money+"\n"
    if(e.player.money > e.street.price)
      string += "bought "+e.street.name
    else
      string += "can´t afford street"
    string
  }

  def getPayRentString(e: payRentEvent): String = e.from.name +" pays rent to " +e.to.name

  def getGameOverString(e: GameOverEvent): String = e.winner.name+ " is the winner!!"

  def getBrokeEventString(e: brokeEvent): String = e.player.name+" is broke!!"

  def getPlayerSellsStreetString(e: playerSellsStreetEvent): String = {
    var string = e.from.name +" sells "+e.street.name+"\n"
    string += "new creditbalance: "+e.from.money
    string
  }
  def getEndRoundString(e:endRoundEvent): String = "\n\n\nround "+ e.round +" ends"

  def getNewRoundString(e:newRoundEvent): String = "\n\nround "+ e.round +" starts"
  def getPlayerMoveToJailString(e: playerMoveToJail): String = e.player.name+" moved to Jail!"
  def getPlayerMovedString(e: playerMoveEvent): String = e.player.name +" moved to "+ e.player.position
}