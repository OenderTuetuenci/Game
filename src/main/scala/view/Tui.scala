package view

import controller._
import model._
import util.Observer

import scala.io.StdIn._
import scala.language.implicitConversions

class Tui(controller: GameController) extends Observer {
    controller.add(this)

    def getController: GameController = controller

    override def update(e: PrintEvent): Any = {
        e match {
            //Input vlt keyboardinput mit callback für cheats für aktuellen spieler (immer die letzten 10 zeichen speichern und vergleichen bei jeder tastatureingabe)
            case e: askUndoGetPlayersEvent => {
                println("Undo?")
                controller.answer = readLine()
            }
            case e: askBuyHomeEvent => askBuyHomeString
            case e: answerEvent => controller.answer = readLine()
            case e: newGameEvent => getPlayerCount
            case e: askBuyEvent => askBuyString
            //Output
            case e: gameIsGoingToStartEvent => println(getGameIsGoingToStartString(e))
            case e: displayRollForPositionsEvent => getRollForPositionsString(e)
            case e: brokeEvent => getBrokeEventString(e)
            case e: gameFinishedEvent => getFinishedString(e)
            case e: payRentEvent => getPayRentString(e)
            case e: buyStreetEvent => getBuyStreetEventString(e)
            case e: buyTrainstationEvent => getBuyTrainstationEventString(e)
            case e: playerInJailEvent => getPlayerInJailString(e)
            case e: normalTurnEvent => getNormalTurnString(e)
            case e: diceEvent => getRollString(e)
            case e: playerSellsStreetEvent => getPlayerSellsStreetString(e)
            case e: playerUsesHyptohekOnStreetEvent => getPlayerUsesHypothekOnStreetString(e)
            case e: playerPaysHyptohekOnStreetEvent => getPlayerPaysHypothekOnStreetString(e)
            case e: newRoundEvent => getNewRoundString(e)
            case e: endRoundEvent => getEndRoundString(e)
            case e: playerMoveToJail => getPlayerMoveToJailString(e)
            case e: optionEvent => getOptionString(e)
            case e: printEverythingEvent => getPlayerAndBoardToString
            case e: playerMoveEvent => getPlayerMovedString(e)
            case e: playerIsFreeEvent => getPlayerIsFreeString(e)
            case e: playerRemainsInJailEvent => getPlayerRemainsInJailString(e)
            case e: playerWentOverGoEvent => getPlayerWentOverGoString(e)
            case e: playerWentOnGoEvent => getPlayerWentOnGoString(e)
            case e: streetOnHypothekEvent => getStreetOnHypothekString(e)
            case e: playerHasDeptEvent => getPlayerHasDeptEventString(e)
            case _ =>
        }

    }

    def askBuyHomeString: Unit = {
        println("Do u want to buy a Home on this Street?")
        controller.answer = readLine()
    }

    def askBuyString: Unit = {
        println("Do u want to buy this street?")
        controller.answer = readLine()
    }

    def getGameIsGoingToStartString(e: gameIsGoingToStartEvent): String = "The Game is going to start."

    def getRollForPositionsString(e: displayRollForPositionsEvent): String = "Players roll for positions."

    def getPlayerAndBoardToString: String = {
        val players = controller.players
        val board = controller.board
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
                case s: Buyable => s.toString
                case s: Los => string += s.toString
                case s: Eventcell => string += s.toString
                case s: CommunityChest => string += s.toString
                case s: IncomeTax => string += s.toString
                case s: Jail => string += s.toString
                case s: Zusatzsteuer => string += s.toString
                case s: FreiParken => string += s.toString
                case s: GoToJail => string += s.toString
            }
            // spieler die sich auf dem aktuellen feld befinden werden angezeigt
            if (playersOnThisField != "") string += " | players on this field: " + playersOnThisField
            string += "\n"
        }
        print(string)
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

    def getEndRoundString(e: endRoundEvent): String = {
        println("\n\n\nround " + e.round + " ends")
        "\n\n\nround " + e.round + " ends"
    }

    def getNewRoundString(e: newRoundEvent): String = "\n\nround " + e.round + " starts"

    def getPlayerMoveToJailString(e: playerMoveToJail): String = e.player.name + " moved to Jail!"

    def getPlayerMovedString(e: playerMoveEvent): String = e.player.name + " moved to " + e.player.position

    def getPlayerHasDeptEventString(e: playerHasDeptEvent): String = e.player.name + " is in minus: " + e.player.money

    def getOptionString(e: optionEvent): String = "option: " + e.option

    def getPlayerCount: Unit = {
        print("How many players?: ") // todo how many npc
        val playerCount = readInt()
        print("how many npc?: ")
        val npcCount = readInt()

    }
}