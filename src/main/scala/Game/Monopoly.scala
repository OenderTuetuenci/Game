package Game

import controller._
import view.Tui

// todo undo pattern spieleranzahl namen wie viele spieler und bots im beforegamestartsstate
//todo option and try wo exceptions kommen koennen oder none

object Monopoly {
    val gameController = new GameController
    val tui = new Tui(gameController)


    def main(args: Array[String]): Unit = {
        while (!gameController.gameOver)
            gameController.run()
    }
}

// todo if player.gameover -> remove player