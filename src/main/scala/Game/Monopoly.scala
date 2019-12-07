package Game
//
//import controller._
//import view.Tui
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
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.HBox
import scalafx.scene.paint.Color._
import scalafx.scene.paint.{LinearGradient, Stops}
import scalafx.scene.text.Text
import java.io.{FileNotFoundException, PrintWriter, StringWriter}

import Game.Monopoly.{button, informationDialog, onQuit, onStartGame}

import scala.language.implicitConversions
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.image.Image
import scalafx.scene.layout.{GridPane, Priority, VBox}
// todo sbt -> run ....https://github.com/scalafx/scalafx-hello-world/tree/SFX-8
//todo dialogs: https://github.com/scalafx/scalafx/blob/master/scalafx-demos/src/main/scala/scalafx/controls/DialogsDemo.scala
object Monopoly extends JFXApp {
    openMainWindow()

    // functions

    def onQuit() = {
        print("quit")
    }

    def onContinue() = {
        // players
        // npc
        // names
        // ok.clicked(gamestate.handle(getplayers(players,npc,names)))

        print("continue")
    }

    def onStartGame() = {
        openGetPlayersWindow() // .exec_()
    }

    // widgets

    def button[R](text: String, action: () => R) = new Button(text) {
        onAction = handle {action()}
        alignmentInParent = Pos.Center
        hgrow = Priority.Always
        maxWidth = Double.MaxValue
        padding = Insets(7)
    }

    // Dialogs

    def textInputDialog(): Unit = {
        val dialog = new TextInputDialog(defaultValue = "walter") {
            initOwner(stage)
            title = "Text Input Dialog"
            headerText = "Look, a Text Input Dialog."
            contentText = "Please enter your name:"
        }

        val result = dialog.showAndWait()
        result match {
            case Some(name) => println("Your name: " + name)
            case None       => println("Dialog was canceled.")
        }
    }

    def informationDialog(): Unit = {
        new Alert(AlertType.Information) {
            initOwner(stage)
            title = "Information Dialog"
            headerText = "Look, an Information Dialog."
            contentText = "I have a great message for you!"
        }.showAndWait()
    }

    // Windows

    def openMainWindow() = {
        stage = new PrimaryStage {
            title = "ScalaFX Hello World"
            scene = new Scene {
                fill = Black
                content = new VBox {
                    padding = Insets(20)
                    children = Seq(
                        button("Start game", onStartGame),
                        button("Information", informationDialog),
                        button("Quit", onQuit),
                    )
                }
            }
        }
    }


    def openGetPlayersWindow() = {
        // open window get players an npc
        stage = new PrimaryStage {
            title = "How many players and npc?"
            scene = new Scene {
                fill = Black
                content = new VBox {
                    padding = Insets(20)
                    children = Seq(
                        new Text {
                            text = "Players"
                            style = "-fx-font-size: 48pt"
                            fill = new LinearGradient(
                                endX = 0,
                                stops = Stops(PaleGreen, SeaGreen))
                        },
                        new TextField {
                            id = "tfPlayerCount"
                            layoutX = 20
                            layoutY = 50
                        },
                        new Text {
                            text = "Npc"
                            style = "-fx-font-size: 48pt"
                            fill = new LinearGradient(
                                endX = 0,
                                stops = Stops(Cyan, DodgerBlue)
                            )
                            effect = new DropShadow {
                                color = DodgerBlue
                                radius = 25
                                spread = 0.25
                            }
                        },
                        new TextField {
                            id = "tfNpcCount"
                            layoutX = 20
                            layoutY = 50
                        },
                        button("Continue", onContinue),
                        button("Quit", onQuit),


                    )}
            }
        }
    }

}
