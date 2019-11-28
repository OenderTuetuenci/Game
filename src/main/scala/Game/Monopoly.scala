package Game

import controller._
import model._
import view.Tui


object Monopoly {
    val gameState = new GameStates()
    val tui = new Tui(gameState)

    val playerController = new PlayerController()
    val boardController = new BoardController()

    def main(args: Array[String]): Unit = {
        //todo wieder state = gamestates.handle
        gameState.handle(beforeGameStartsEvent(tui.getPlayerCount()))
        gameState.handle(createPlayersEvent())
        gameState.handle(createBoardEvent())
        gameState.handle(rollForPositionsEvent())
        do {
            gameState.handle(runRoundEvent())
            gameState.handle(checkGameOverEvent())
        } while (!gameState.gameOver) //todo wieder while not state == gameoverstate
        gameState.handle(gameOverEvent())
    }
}

// todo if player.gameover -> remove player