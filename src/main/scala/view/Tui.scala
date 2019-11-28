package view

import Game.Monopoly.gameState._
import controller._
import model._
import util.Observer

import scala.io.StdIn._

class Tui(controller: GameStates) extends Observer {
    controller.add(this)

    def getController: GameStates = controller

    override def update(e: PrintEvent): Boolean = {
        var worked = true
        var string = ""
        e match {
            case e: gameIsGoingToStartEvent => string = getGameIsGoingToStartString(e)
            case e: displayRollForPositionsEvent => string = getRollForPositionsString(e)
            case e: brokeEvent => string = getBrokeEventString(e)
            case e: gameFinishedEvent => string = getFinishedString(e)
            case e: payRentEvent => string = getPayRentString(e)
            case e: buyStreetEvent => string = getBuyStreetEventString(e)
            case e: buyTrainstationEvent => string = getBuyTrainstationEventString(e)
            case e: playerInJailEvent => string = getPlayerInJailString(e)
            case e: normalTurnEvent => string = getNormalTurnString(e)
            case e: diceEvent => string = getRollString(e)
            case e: playerSellsStreetEvent => string = getPlayerSellsStreetString(e)
            case e: playerUsesHyptohekOnStreetEvent => string = getPlayerUsesHypothekOnStreetString(e)
            case e: playerPaysHyptohekOnStreetEvent => string = getPlayerPaysHypothekOnStreetString(e)
            case e: playerUsesHyptohekOnTrainstationEvent => string = getPlayerUsesHypothekOnTrainstationString(e)
            case e: playerPaysHyptohekOnTrainstationEvent => string = getPlayerPaysHypothekOnTrainstationString(e)
            case e: playerSellsTrainstationEvent => string = getPlayerSellsTrainstationString(e)
            case e: newRoundEvent => string = getNewRoundString(e)
            case e: endRoundEvent => string = getEndRoundString(e)
            case e: playerMoveToJail => string = getPlayerMoveToJailString(e)
            case e: optionEvent => string = getOptionString(e)
            case e: printEverythingEvent => string = getPlayerAndBoardToString
            case e: playerMoveEvent => string = getPlayerMovedString(e)
            case e: playerIsFreeEvent => string = getPlayerIsFreeString(e)
            case e: playerRemainsInJailEvent => string = getPlayerRemainsInJailString(e)
            case e: playerWentOverGoEvent => string = getPlayerWentOverGoString(e)
            case e: playerWentOnGoEvent => string = getPlayerWentOnGoString(e)
            case e: streetOnHypothekEvent => string = getStreetOnHypothekString(e)
            case e: playerHasDeptEvent => string = getPlayerHasDeptEventString(e)
            case _ => worked = false
        }
        println(string)
        worked
    }

    def getGameIsGoingToStartString(e: gameIsGoingToStartEvent): String = "The Game is going to start."

    def getRollForPositionsString(e: displayRollForPositionsEvent): String = "Players roll for positions."

    def getPlayerAndBoardToString: String = {
        var string = "Spieler und Spielfeld:\n\n"
        string += "\nSpieler: "
        for (player <- players) string += player.toString + "\n"
        string += "\nSpielfeld:\n"
        for (i <- board.indices) {
            // spieler die noch in der runde sind raussuchen
            var playersOnThisField = ""
            for (player <- players) {
                if (player.money > 0 && player.position == i) {
                    playersOnThisField += player.name + " "
                }
            }
            // felder anzeigen
            board(i) match {
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

    def getRollString(e: diceEvent): String = { //SS
        var string = "throwing Dice:\n"
        string += "rolled :" + e.eyeCount1 + " " + e.eyeCount2 + "\n"
        if (e.pasch)
            string += "rolled pasch!"
        string
    }

    def getNormalTurnString(e: normalTurnEvent): String = {
        val string = "Its " + e.player.name + " turn!\n"
        string
    }

    def getPlayerInJailString(e: playerInJailEvent): String = {
        var string = "\nIts " + e.player.name + " turn. he is in jail!\n"
        string += "Jailcount: " + (e.player.jailCount + 1) + "\n"
        string
    }

    def getPlayerIsFreeString(e: playerIsFreeEvent): String = e.player.name + " is free again!"

    def getPlayerRemainsInJailString(e: playerRemainsInJailEvent): String = e.player.name + " remains in jail"

    def getStreetOnHypothekString(e: streetOnHypothekEvent): String = e.street.name + " is on hypothek."

    def getBuyStreetEventString(e: buyStreetEvent): String = {
        var string = e.player.money + "\n"
        if (e.player.money > e.street.price)
            string += "bought " + e.street.name
        else
            string += "can´t afford street"
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

    def getFinishedString(e: gameFinishedEvent): String = e.winner.name + " is the winner!!"

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

    def getPlayerHasDeptEventString(e: playerHasDeptEvent): String = e.player.name + " is in minus: " + e.player.money

    def getOptionString(e: optionEvent): String = "option: " + e.option

    def getPlayerCount = {
        print("How many players?: ") // todo how many npc
        val playerCount = readInt()
        print("how many npc?: ")
        val npcCount = readInt()

        val playerNames: Array[String] = Array.ofDim(playerCount)
        val npcNames: Array[String] = Array.ofDim(npcCount)
        // spieler mit namen einlesensr
        for (i <- 0 until playerCount) {
            println("Enter name player" + (i + 1) + ":")
            playerNames(i) = readLine()
        }
        for (i <- 0 until npcCount) {
            npcNames(i) = "NPC " + (i + 1)
        }
        (playerCount, npcCount, playerNames, npcNames)
    }
}
