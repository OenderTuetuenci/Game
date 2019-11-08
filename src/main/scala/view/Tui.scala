package view

import controller.Controller
import util.Observer

import io.StdIn._

class Tui(controller: Controller) extends Observer{
  controller.add(this)

  override def update: Unit = println(controller.getPlayerAndBoardToString)
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
