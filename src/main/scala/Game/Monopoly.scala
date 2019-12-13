package Game

import controller._
import model.OpenMainWindowEvent
import view.{Gui, Tui}
//// todo undo pattern spieleranzahl namen wie viele spieler und bots im beforegamestartsstate
////todo option and try wo exceptions kommen koennen oder none
////
//object Monopoly {
//  val gameController = new GameController
//  val tui = new Tui(gameController)
//
//  def main(args: Array[String]) = gameController.run()
//}
//

//// todo if player.gameover -> remove player
// todo currentPlayer = players(isturn)
import scalafx.application.JFXApp

import scala.language.implicitConversions
// todo echte implementierung und stub implementierung
//komponenten sollen keinen zugriff mehr auf innere klassen ander komponenten haben
// mocks bei componententests
// https://xebia.com/blog/try-option-or-either/
// callbacks https://github.com/japgolly/scalajs-react/blob/master/doc/CALLBACK.md
// menubar http://sjgpsoft.blogspot.com/2016/02/scalafx-menu-basics.html
// sbt -> run ....https://github.com/scalafx/scalafx-hello-world/tree/SFX-8
// dialogs: https://github.com/scalafx/scalafx/blob/master/scalafx-demos/src/main/scala/scalafx/controls/DialogsDemo.scala
object Monopoly extends JFXApp {
    val gameController = new GameController
    val gui = new Gui(gameController)
    val tui = new Tui(gameController)

    gameController.notifyObservers(OpenMainWindowEvent())
}