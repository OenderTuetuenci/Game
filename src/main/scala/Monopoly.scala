import controller.{Controller, GameStates}
import model._
import view.Tui

object Monopoly {
    val controller = new Controller()
    val tui = new Tui(controller)

    def main(args: Array[String]): Unit = {
        GameStates.handle(beforeGameStartsEvent())
        tui.getPlayerCount

        // todo controller.letPlayersRollForPositions  ...vlt auch wo anders
        controller.notifyObservers(printEverythingEvent())
        do {
            GameStates.handle(checkGameOverEvent())
        } while (!GameStates.gameOver) // while not state = gameoverstate
    }
}
