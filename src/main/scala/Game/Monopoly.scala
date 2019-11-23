package Game

import controller._
import model._
import view.Tui

trait Monopoly {
    // global vars
    val controller = new Controller()
    val playerController = new PlayerController()
    val boardController = new BoardController()
    val tui = new Tui(controller)
    val dice = Dice()
    var gameState = GameStates.handle(beforeGameStartsEvent())
    var board: Vector[Cell] = Vector[Cell]()
    var playerCount = 0
    var players: Vector[Player] = Vector[Player]()
    var isturn = 0 // aktueller spieler
    var round = 1
}

object Game extends Monopoly {
    def main(args: Array[String]): Unit = {
        val playerCountAndNames = tui.getPlayerCount
        gameState = GameStates.handle(createPlayersEvent(playerCountAndNames._1, playerCountAndNames._2))
        gameState = GameStates.handle(createBoardEvent())
        // todo controller.letPlayersRollForPositions  ...vlt auch wo anders

        controller.notifyObservers(printEverythingEvent())
        do {
            gameState = GameStates.handle(runRoundEvent())
            gameState = GameStates.handle(checkGameOverEvent())
        } while (!(gameState == GameStates.gameOverState))
        gameState = GameStates.handle(gameOverEvent())
    }
}

// todo if player.gameover -> remove player
