package Game

import controller._
import model.OpenMainWindowEvent
import scalafx.scene.control.Label
import view.Tui
//
//// todo undo pattern spieleranzahl namen wie viele spieler und bots im beforegamestartsstate
////todo option and try wo exceptions kommen koennen oder none
//
//object Monopoly {
//  val gameController = new GameController
//  val tui = new Tui(gameController)
//
//  def main(args: Array[String]) = gameController.run()
//}
//
//// todo if player.gameover -> remove player

import scalafx.application.JFXApp

import scala.language.implicitConversions

// todo sbt -> run ....https://github.com/scalafx/scalafx-hello-world/tree/SFX-8
//todo dialogs: https://github.com/scalafx/scalafx/blob/master/scalafx-demos/src/main/scala/scalafx/controls/DialogsDemo.scala
object Monopoly extends JFXApp {
    val gameController = new GameController
    val tui = new Tui(gameController)
    gameController.notifyObservers(OpenMainWindowEvent())
    tryDrawOnGui()

    def tryDrawOnGui(): Unit = {
        val playerLabel = new Label("onstack\nPlayer\nLabel")
        val scene = gameController.currentStage.scene
        val stackpane = scene().lookup("#stackpane")
    }

    //val stackpane2 = stackpane.asInstanceOf[StackPane]
    //stackpane2.getChildren()
    //stackpane.getChildren.add(playerLabel)
    //movePlayerG

}
