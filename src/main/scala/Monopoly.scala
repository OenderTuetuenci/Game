package Game

import com.google.inject.Guice
import controller.controllerComponent.ControllerInterface
import model.OpenMainWindowEvent
import view.{Gui, Tui}
///// todo undo pattern spieleranzahl namen wie viele spieler und bots im beforegamestartsstate
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
    val injector = Guice.createInjector(new MonopolyModule)
    val controller = injector.getInstance(classOf[ControllerInterface])
    val gui = new Gui(controller)
    val tui = new Tui(controller)
    controller.notifyObservers(OpenMainWindowEvent())
}