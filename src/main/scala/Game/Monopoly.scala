package Game

import controller._
import model._
import view.Tui

trait Monopoly {
    // global vars
    val tuiController = new TuiController()
    val playerController = new PlayerController()
    val boardController = new BoardController()
    val tui = new Tui(tuiController)
    val dice = Dice()
    var board: Vector[Cell] = Vector[Cell]()
    var playerCount = 0
    var players: Vector[Player] = Vector[Player]()
    var isturn = 0 // aktueller spieler
    var round = 1
}

object Game extends Monopoly {
    def main(args: Array[String]): Unit = {
        //todo wieder state = gamestates.handle
        val playerCountAndNames = tui.getPlayerCount
        GameStates.handle(beforeGameStartsEvent())
        GameStates.handle(createPlayersEvent(playerCountAndNames._1, playerCountAndNames._2))
        GameStates.handle(createBoardEvent())
        // todo controller.letPlayersRollForPositions  ...vlt auch wo anders

        tuiController.notifyObservers(printEverythingEvent())
        do {
            GameStates.handle(runRoundEvent())
            GameStates.handle(checkGameOverEvent())
        } while (!GameStates.gameOver) //todo wieder while not state == gameoverstate
        GameStates.handle(gameOverEvent())
    }
}

// todo if player.gameover -> remove player
