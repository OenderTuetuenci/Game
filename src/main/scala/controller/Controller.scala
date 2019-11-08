package controller

import model.{Cell, CommunityChest, Elektrizitaetswerk, Eventcell, FreiParken, GameOverEvent, GoToJail, IncomeTax, Jail, Los, Player, Street, Trainstation, Wasserwerk, Zusatzsteuer, buyEvent, diceEvent, normalTurnEvent, payRentEvent, playerInJailEvent}
import util.Observable

class Controller extends Observable {

  var spielBrett: Array[Cell] = createSpielBrett
  var playerCount = 0
  var players:Array[Player] = _
  var amzug = 0 // aktueller spieler
  var runde = 1

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
  def getRollString(e: diceEvent): String = {
    var string = "throwing Dice:\n"
    string += "rolled :"+e.eyeCount1+" "+e.eyeCount2+"\n"
    if(e.pasch == true)
      string += "rolled pasch!"
    string
  }

  def getNormalTurnString(e: normalTurnEvent): String = {
    var string = "Its "+e.player.name+" turn!"
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

  def getBuyEventString(e: buyEvent): Any = ???

  def getPayRentString(e: payRentEvent): Any = ???

  def getGameOverString(e: GameOverEvent): Any = ???

}
