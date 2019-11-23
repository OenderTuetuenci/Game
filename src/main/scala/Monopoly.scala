import controller.{Controller, GameStates}
import model._
import view.Tui

object Monopoly {
    val controller = new Controller()
    val tui = new Tui(controller)

    def main(args: Array[String]): Unit = {
        var gameState = GameStates.handle(beforeGameStartsEvent())
        tui.getPlayerCount
        // todo controller.letPlayersRollForPositions  ...vlt auch wo anders

        controller.notifyObservers(printEverythingEvent())
        do {
            gameState = GameStates.handle(checkGameOverEvent())
        } while (!GameStates.gameOver) // while not state = gameoverstate
        //} while (!gameState == GameStates.gameOverState) // while not state = gameoverstate
        gameState = GameStates.handle(gameOverEvent())
    }
}
