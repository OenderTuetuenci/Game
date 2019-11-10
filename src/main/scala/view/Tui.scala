package view

import controller.Controller
import model._
import util.Observer

import scala.io.StdIn._

class Tui(controller: Controller) extends Observer {
    controller.add(this)

    override def update(e: Event) = {
        e match {
            case e: brokeEvent => println(controller.getBrokeEventString(e))
            case e: gameOverEvent => println(controller.getGameOverString(e))
            case e: payRentEvent => println(controller.getPayRentString(e))
            case e: buyStreetEvent => println(controller.getBuyStreetEventString(e))
            case e: buyTrainstationEvent => println(controller.getBuyTrainstationEventString(e))
            case e: playerInJailEvent => println(controller.getPlayerInJailString(e))
            case e: normalTurnEvent => println(controller.getNormalTurnString(e))
            case e: diceEvent => println(controller.getRollString(e))
            case e: playerSellsStreetEvent => println(controller.getPlayerSellsStreetString(e))
            case e: playerUsesHyptohekOnStreetEvent => println(controller.getPlayerUsesHypothekOnStreetString(e))
            case e: playerPaysHyptohekOnStreetEvent => println(controller.getPlayerPaysHypothekOnStreetString(e))
            case e: playerUsesHyptohekOnTrainstationEvent => println(controller.getPlayerUsesHypothekOnTrainstationString(e))
            case e: playerPaysHyptohekOnTrainstationEvent => println(controller.getPlayerPaysHypothekOnTrainstationString(e))
            case e: playerSellsTrainstationEvent => println(controller.getPlayerSellsTrainstationString(e))
            case e: newRoundEvent => println(controller.getNewRoundString(e))
            case e: endRoundEvent => println(controller.getEndRoundString(e))
            case e: playerMoveToJail => println(controller.getPlayerMoveToJailString(e))
            case e: optionEvent => println("option: " + e.option)
            case e: printEverythingEvent => println(controller.getPlayerAndBoardToString)
            case e: playerMoveEvent => println(controller.getPlayerMovedString(e))
            case e: playerIsFreeEvent => println(controller.getPlayerIsFreeString(e))
            case e: playerRemainsInJailEvent => println(controller.getPlayerRemainsInJailString(e))
            case e: playerWentOverGoEvent => println(controller.getPlayerWentOverGoString(e))
            case e: playerWentOnGoEvent => println(controller.getPlayerWentOnGoString(e))
            case e: streetOnHypothekEvent => println(controller.getStreetOnHypothekString(e))
            case e: playerHasDeptEvent => println(controller.getPlayerHasDeptEventString(e))
        }
    }

    def getPlayerCount: Unit = {
        print("How many players?: ") // todo how many npc
        val playerCount = readInt()
        val playerNames: Array[String] = Array.ofDim(playerCount)
        // spieler mit namen einlesensr
        for (i <- 0 until playerCount) {
            println("Enter name player" + (i + 1) + ":")
            playerNames(i) = readLine()
        }
        controller.createPlayers(playerCount, playerNames)
    }
}
