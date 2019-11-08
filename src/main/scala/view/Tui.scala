package view

import controller.Controller
import model.{Event, GameOverEvent, buyEvent, diceEvent, normalTurnEvent, payRentEvent, playerInJailEvent}
import util.Observer

import io.StdIn._

class Tui(controller: Controller) extends Observer{
  controller.add(this)
  override def update(e:Event) = {
    e match {
      case e:GameOverEvent => controller.getGameOverString(e)
      case e:payRentEvent => controller.getPayRentString(e)
      case e:buyEvent => controller.getBuyEventString(e)
      case e:playerInJailEvent => controller.getPlayerInJailString(e)
      case e:normalTurnEvent => controller.getNormalTurnString(e)
      case e:diceEvent => controller.getDiceString(e)
      case e:_ =>println(controller.getPlayerAndBoardToString)
    }
  }

  def getPlayerCount():Unit = {
    println("Playercount?")
    val playerCount = readInt()
    val playerNames = Array.ofDim[String](playerCount)
    // spieler mit namen einlesensr
    for (i <- 0 until playerCount) {
      println("Enter name player" + (i + 1) + ":")
      playerNames(i) = readLine()
    }
    controller.createPlayers(playerCount,playerNames)
  }
}
