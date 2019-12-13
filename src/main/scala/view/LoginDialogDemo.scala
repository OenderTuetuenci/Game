package scalafx.controls

import scalafx.Includes._
import scalafx.application.{JFXApp, Platform}
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control._
import scalafx.scene.layout.{GridPane, VBox}
//todo https://github.com/scalafx/scalafx/blob/master/scalafx-demos/src/main/scala/scalafx/controls/LoginDialogDemo.scala

//todo properties
//override def main(args: Array[String]) = {
//    currentStage.show()
//gameController.run()
//}


//    val playerCount = new IntegerProperty(this, "playerCount", 0) {
//        onChange { (_, oldValue, newValue) =>
//            println(
//                s"Value of property '$name' is changing from $oldValue to $newValue")
//        }
//    }
//    val npcCount = new IntegerProperty(this, "npcCount", 0) {
//        onChange { (_, oldValue, newValue) =>
//            println(
//                s"Value of property '$name' is changing from $oldValue to $newValue")
//        }
//    }
// val playernames =

// widgets
object LoginDialogDemo extends JFXApp {

    stage = new JFXApp.PrimaryStage {
        //icons += new Image("/scalafx/sfx.png")
        scene = new Scene {
            title = "Custom Dialog Demo"
            content = new VBox {
                children = new Button("Show Login Dialog") {
                    onAction = handle {
                        onShowLoginDialog()
                    }
                }
                padding = Insets(top = 24, right = 64, bottom = 24, left = 64)
            }
        }
    }

    def onShowLoginDialog(): Unit = {

        case class Result(playerCount: String, npcCount: String)

        // Create the custom dialog.
        val dialog = new Dialog[Result]() {
            initOwner(stage)
            title = "Start Game"
            headerText = "How many players and npc"
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }

        // Set the button types.
        val startButtonType = new ButtonType("Start", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(startButtonType, ButtonType.Cancel)

        // Create the username and password labels and fields.
        val tfPlayerCount = new TextField() {
            promptText = "playerCount"
        }
        val tfNpcCount = new PasswordField() {
            promptText = "npcCount"
        }

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)

            add(new Label("Players:"), 0, 0)
            add(tfPlayerCount, 1, 0)
            add(new Label("Npc:"), 0, 1)
            add(tfNpcCount, 1, 1)
        }

        // Enable/Disable login button depending on whether a username was entered.
        val startButton = dialog.dialogPane().lookupButton(startButtonType)
        startButton.disable = true

        // Do some validation (disable when username is empty).
        tfPlayerCount.text.onChange { (_, _, newValue) => startButton.disable = newValue.trim().isEmpty }
        tfNpcCount.text.onChange { (_, _, newValue) => startButton.disable = newValue.trim().isEmpty }

        dialog.dialogPane().content = grid

        // Request focus on the username field by default.
        Platform.runLater(tfPlayerCount.requestFocus())

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.resultConverter = dialogButton =>
            if (dialogButton == startButtonType) Result(tfPlayerCount.text(), tfNpcCount.text())
            else null

        val result = dialog.showAndWait()

        result match {
            case Some(Result(p, npc)) =>
                println("playercount=" + p + ", npccount=" + npc)
            case None => println("Dialog returned: None")
        }
    }

}