package view

import controller._
import model._
import scalafx.Includes.{handle, _}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.Platform
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout._
import scalafx.scene.paint.Color.{Black, PaleGreen, SeaGreen}
import scalafx.scene.paint.{LinearGradient, Stops}
import scalafx.scene.text.Text
import util.Observer

import scala.io.StdIn._
import scala.language.implicitConversions

class Tui(controller: GameController) extends Observer {
    controller.add(this)

    def getController: GameController = controller

    override def update(e: PrintEvent): Any = {
        var worked = true
        var string = ""
        e match {
            //GUI
            case e: OpenMainWindowEvent => mainWindow(e)
            case e: OpenGameWindowEvent => gameWindow(e)
            case e: OpenGetPlayersDialogEvent => getPlayersDialog(e)
            case e: OpenGetNameDialogEvent => getPlayerNameDialog(e)
            case e: OpenRollDiceDialogEvent => rollDiceDialog(e)
            case e: OpenRollForPosDialogEvent => rollForPosDialog(e)
            case e: OpenInformationDialogEvent => informationDialog(e)
            case e: OpenConfirmationDialogEvent => confirmationDialog(e)
            case e: OpenInJailDialogEvent => inJailDialog(e)


            //Input
            case e: askUndoGetPlayersEvent => {
                println("Undo?")
                controller.answer = readLine()
            }
            case e: askBuyHomeEvent => askBuyHomeString
            case e: answerEvent => controller.answer = readLine()
            case e: newGameEvent => getPlayerCount
            case e: askBuyEvent => askBuyString
            //Output
            case e: gameIsGoingToStartEvent => string = getGameIsGoingToStartString(e)
            case e: displayRollForPositionsEvent => string = getRollForPositionsString(e)
            case e: brokeEvent => string = getBrokeEventString(e)
            case e: gameFinishedEvent => string = getFinishedString(e)
            case e: payRentEvent => string = getPayRentString(e)
            case e: buyStreetEvent => string = getBuyStreetEventString(e)
            case e: buyTrainstationEvent => string = getBuyTrainstationEventString(e)
            case e: playerInJailEvent => string = getPlayerInJailString(e)
            case e: normalTurnEvent => string = getNormalTurnString(e)
            case e: diceEvent => string = getRollString(e)
            case e: playerSellsStreetEvent => string = getPlayerSellsStreetString(e)
            case e: playerUsesHyptohekOnStreetEvent => string = getPlayerUsesHypothekOnStreetString(e)
            case e: playerPaysHyptohekOnStreetEvent => string = getPlayerPaysHypothekOnStreetString(e)
            case e: playerUsesHyptohekOnTrainstationEvent => string = getPlayerUsesHypothekOnTrainstationString(e)
            case e: playerPaysHyptohekOnTrainstationEvent => string = getPlayerPaysHypothekOnTrainstationString(e)
            case e: playerSellsTrainstationEvent => string = getPlayerSellsTrainstationString(e)
            case e: newRoundEvent => string = getNewRoundString(e)
            case e: endRoundEvent => string = getEndRoundString(e)
            case e: playerMoveToJail => string = getPlayerMoveToJailString(e)
            case e: optionEvent => string = getOptionString(e)
            case e: printEverythingEvent => getPlayerAndBoardToString
            case e: playerMoveEvent => string = getPlayerMovedString(e)
            case e: playerIsFreeEvent => string = getPlayerIsFreeString(e)
            case e: playerRemainsInJailEvent => string = getPlayerRemainsInJailString(e)
            case e: playerWentOverGoEvent => string = getPlayerWentOverGoString(e)
            case e: playerWentOnGoEvent => string = getPlayerWentOnGoString(e)
            case e: streetOnHypothekEvent => string = getStreetOnHypothekString(e)
            case e: playerHasDeptEvent => string = getPlayerHasDeptEventString(e)
        }
    }

    def askBuyHomeString: Unit = {
        println("Do u want to buy a Home on this Street?")
        controller.answer = readLine()
    }

    def askBuyString: Unit = {
        println("Do u want to buy this street?")
        controller.answer = readLine()
    }

    def getGameIsGoingToStartString(e: gameIsGoingToStartEvent): String = "The Game is going to start."

    def getRollForPositionsString(e: displayRollForPositionsEvent): String = "Players roll for positions."

    def getPlayerAndBoardToString: String = {
        val players = controller.players
        val board = controller.board
        var string = "Spieler und Spielfeld:\n\n"
        string += "\nSpieler: "
        for (player <- players) string += player.toString + "\n"
        string += "\nSpielfeld:\n"
        for (i <- board.indices) {
            // spieler die noch in der runde sind raussuchen
            var playersOnThisField = ""
            for (player <- players) {
                if (player.money > 0 && player.position == i) {
                    playersOnThisField += player.name + " "
                }
            }
            // felder anzeigen
            board(i) match {
                case s: Los => string += s.toString
                case s: Eventcell => string += s.toString
                case s: CommunityChest => string += s.toString
                case s: IncomeTax => string += s.toString
                case s: Jail => string += s.toString
                case s: Elektrizitaetswerk => string += s.toString
                case s: Wasserwerk => string += s.toString
                case s: Zusatzsteuer => string += s.toString
                case s: FreiParken => string += s.toString
                case s: GoToJail => string += s.toString
                case s: Street =>
                    // besitzer des feldes suchen
                    var owner = s.owner.toString
                    if (s.owner != -1) owner = players(s.owner).name
                    string += s.toString + " | Owner: " + owner
                case s: Trainstation =>
                    // besitzer des feldes suchen
                    var owner = s.owner.toString
                    if (s.owner != -1) owner = players(s.owner).name
                    string += s.toString + " | Owner: " + owner
                case s: Elektrizitaetswerk =>
                    // besitzer des feldes suchen
                    var owner = s.owner.toString
                    if (s.owner != -1) owner = players(s.owner).name
                    string += s.toString + " | Owner: " + owner
                case s: Wasserwerk =>
                    // besitzer des feldes suchen
                    var owner = s.owner.toString
                    if (s.owner != -1) owner = players(s.owner).name
                    string += s.toString + " | Owner: " + owner
            }
            // spieler die sich auf dem aktuellen feld befinden werden angezeigt
            if (playersOnThisField != "") string += " | players on this field: " + playersOnThisField
            string += "\n"
        }
        print(string)
        string
    }

    def getRollString(e: diceEvent): String = { //SS
        var string = "throwing Dice:\n"
        string += "rolled :" + e.eyeCount1 + " " + e.eyeCount2 + "\n"
        if (e.pasch)
            string += "rolled pasch!"
        string
    }

    def getNormalTurnString(e: normalTurnEvent): String = {
        val string = "Its " + e.player.name + " turn!\n"
        string
    }

    def getPlayerInJailString(e: playerInJailEvent): String = {
        var string = "\nIts " + e.player.name + " turn. he is in jail!\n"
        string += "Jailcount: " + (e.player.jailCount + 1) + "\n"
        string
    }

    def getPlayerIsFreeString(e: playerIsFreeEvent): String = e.player.name + " is free again!"

    def getPlayerRemainsInJailString(e: playerRemainsInJailEvent): String = e.player.name + " remains in jail"

    def getStreetOnHypothekString(e: streetOnHypothekEvent): String = e.street.name + " is on hypothek."

    def getBuyStreetEventString(e: buyStreetEvent): String = {
        var string = e.player.money + "\n"
        if (e.player.money > e.street.price)
            string += "bought " + e.street.name
        else
            string += "can´t afford street"
        string
    }

    def getBuyTrainstationEventString(e: buyTrainstationEvent): String = {
        var string = e.player.money + "\n"
        if (e.player.money > e.street.price)
            string += "bought " + e.street.name
        else
            string += "can´t afford street"
        string
    }

    def getPayRentString(e: payRentEvent): String = e.from.name + " pays rent to " + e.to.name

    def getPlayerWentOverGoString(e: playerWentOverGoEvent): String = e.player.name + " went over go."

    def getPlayerWentOnGoString(e: playerWentOnGoEvent): String = e.player.name + " went on go and gets extra money."

    def getFinishedString(e: gameFinishedEvent): String = e.winner.name + " is the winner!!"

    def getBrokeEventString(e: brokeEvent): String = e.player.name + " is broke!!"

    def getPlayerUsesHypothekOnStreetString(e: playerUsesHyptohekOnStreetEvent): String = {
        e.player.name + " gets Hypothek for " + e.street.name + " new creditbalance: " + e.player.money

    }

    def getPlayerPaysHypothekOnStreetString(e: playerPaysHyptohekOnStreetEvent): String = {
        e.player.name + " pays Hypothek for " + e.street.name + " new creditbalance: " + e.player.money
    }

    def getPlayerSellsStreetString(e: playerSellsStreetEvent): String = {
        var string = e.from.name + " sells " + e.street.name + "\n"
        string += "new creditbalance: " + e.from.money
        string
    }

    def getPlayerUsesHypothekOnTrainstationString(e: playerUsesHyptohekOnTrainstationEvent): String = {
        e.player.name + " pays Hypothek for " + e.street.name + " new creditbalance: " + e.player.money
    }

    def getPlayerPaysHypothekOnTrainstationString(e: playerPaysHyptohekOnTrainstationEvent): String = {
        e.player.name + " pays Hypothek for " + e.street.name + " new creditbalance: " + e.player.money
    }

    def getPlayerSellsTrainstationString(e: playerSellsTrainstationEvent): String = {
        var string = e.from.name + " sells " + e.street.name + "\n"
        string += "new creditbalance: " + e.from.money
        string
    }

    def getEndRoundString(e: endRoundEvent): String = {
        println("\n\n\nround " + e.round + " ends")
        "\n\n\nround " + e.round + " ends"
    }

    def getNewRoundString(e: newRoundEvent): String = "\n\nround " + e.round + " starts"

    def getPlayerMoveToJailString(e: playerMoveToJail): String = e.player.name + " moved to Jail!"

    def getPlayerMovedString(e: playerMoveEvent): String = e.player.name + " moved to " + e.player.position

    def getPlayerHasDeptEventString(e: playerHasDeptEvent): String = e.player.name + " is in minus: " + e.player.money

    def getOptionString(e: optionEvent): String = "option: " + e.option

    def getPlayerCount: Unit = {
        print("How many players?: ") // todo how many npc
        val playerCount = readInt()
        print("how many npc?: ")
        val npcCount = readInt()


    }

    // todo gui /////////////////////////////////////////////////////////////////////////

    // widgets

    def button[R](text: String, action: () => R) = new Button(text) {
        onAction = handle {
            action()
        }
        alignmentInParent = Pos.Center
        hgrow = Priority.Always
        maxWidth = Double.MaxValue
        padding = Insets(7)
    }

    // windows
    // todo mainwindow als menubar von gamewindow dann kann man auch spiele neustarten ohne das programm neu zu starten
    def mainWindow(e: OpenMainWindowEvent) = {
        controller.currentStage = new PrimaryStage {
            title = "Monopoly SE"
            scene = new Scene {
                fill = Black
                content = new VBox {
                    padding = Insets(20)
                    children = Seq(
                        new Text {
                            text = "Monopoly"
                            style = "-fx-font-size: 48pt"
                            fill = new LinearGradient(
                                endX = 0,
                                stops = Stops(PaleGreen, SeaGreen))
                        },
                        button("Start game", controller.onStartGame),
                        button("Information", controller.onInformation),
                        button("Quit", controller.onQuit)
                    )
                }
                }

            }
        }


    def gameWindow(e: OpenGameWindowEvent) = {
        val menubar = new MenuBar {
            menus = List(
                new Menu("File") {
                    items = List(
                        new MenuItem("New..."),
                        new MenuItem("Save")
                    )
                },
                new Menu("Edit") {
                    items = List(
                        new MenuItem("Cut"),
                        new MenuItem("Copy"),
                        new MenuItem("Paste")
                    )
                },
                new Menu("Help") {
                    items = List(
                        new MenuItem("About")
                    )
                }
            )
        }
        menubar.setId("menubar")
        controller.currentStage = new PrimaryStage {
            title = "Monopoly SE"
            scene = new Scene(1100, 800) {
                fill = Black
                content = new HBox {
                    padding = Insets(10)
                    val pane = new StackPane()
                    pane.setId("stackpane")
                    val boardImage = new ImageView(new Image("file:images/board.jpg", 800, 800, true, true))
                    new VBox(
                        new Text {
                            text = "Monopoly"
                            style = "-fx-font-size: 48pt"
                            fill = new LinearGradient(
                                endX = 0,
                                stops = Stops(PaleGreen, SeaGreen))
                        },
                        button("Start game", controller.onStartGame),
                        button("Information", controller.onInformation),
                    )

                    pane.children = List(boardImage)
                    children = Seq(menubar, pane)
                }
            }

        }
        import javafx.stage.Screen

        val screen: Screen = Screen.getPrimary
        val bounds = screen.getVisualBounds

        controller.currentStage.setX(bounds.getMinX)
        controller.currentStage.setY(bounds.getMinY)
        controller.currentStage.setWidth(bounds.getWidth)
        controller.currentStage.setHeight(bounds.getHeight)
    }


    // Dialogs

    def getPlayersDialog(e: OpenGetPlayersDialogEvent): (String, String) = {

        case class Result(playerCount: String, npcCount: String)

        // create dialog
        val dialog = new Dialog[Result]() {
            //initOwner(e.stage)
            title = "Start Game"
            headerText = "How many players and npc"
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        // Set the button types.
        val startButtonType = new ButtonType("Start", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(startButtonType, ButtonType.Cancel)
        // Create labels and fields.
        val tfPlayerCount = new TextField() {
            promptText = "playerCount"
        }
        val tfNpcCount = new TextField() {
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
        // Enable/Disable button depending on whether a username was entered.
        val startButton = dialog.dialogPane().lookupButton(startButtonType)
        //startButton.disable = true
        // todo validation players + npc <= 8
        // tfPlayerCount.text.onChange { (_, _, newValue) => startButton.disable = tfPlayerCount.text().toInt + tfNpcCount.text().toInt <= 8 && newValue.trim().isEmpty}
        dialog.dialogPane().content = grid
        // Request focus on the username field by default.
        Platform.runLater(tfPlayerCount.requestFocus())
        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.resultConverter = dialogButton =>
            if (dialogButton == startButtonType) Result(tfPlayerCount.text(), tfNpcCount.text())
            else null

        val result = dialog.showAndWait()

        result match {
            case Some(Result(p, npc)) => {
                controller.humanPlayers = p.toInt
                controller.npcPlayers = npc.toInt
                (p, npc)
            }
            case None => ("Dialog returned", "None")
        }
    }


    def getPlayerNameDialog(e: OpenGetNameDialogEvent) = {
        //todo liste der spielfiguren die noch nicht gepickt wurden
        case class Result(playerName: String, figure: String)

        // Create the custom dialog.
        val dialog = new Dialog[Result]() {
            title = "Enter Player names:"
            headerText = "Player " + e.currPlayer + " enter name"
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        dialog.getDialogPane.setPrefSize(600, 500)
        val startButtonType = new ButtonType("Start", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(startButtonType, ButtonType.Cancel)

        val tfPlayerName = new TextField() {
            promptText = "Enter name"
        }

        val comboBox = new ComboBox[String]()

        comboBox.getItems().addAll(controller.remainingFiguresToPick) // bilder hinzufuegen
        comboBox.getSelectionModel.select(0) // das 1. element vorher schon auswählen
        val initImg = comboBox.getSelectionModel.getSelectedItem.toString match {
            case "Hut" => "file:images/Hat.jpg"
            case "Fingerhut" => "file:images/Fingerhut.jpg"
            case "Schubkarre" => "file:images/Schubkarre.jpg"
            case "Schuh" => "file:images/Schuh.jpg"
            case "Hund" => "file:images/Hund.jpg"
            case "Auto" => "file:images/Auto.jpg"
            case "Bügeleisen" => "file:images/Buegeleisen.jpg"
            case "Fingerhut" => "file:images/Fingerhut.jpg"
            case "Schiff" => "file:images/Schiff.jpg"
        }
        val image = new ImageView(new Image(initImg,
            200,
            200,
            true,
            true))
        comboBox.value.onChange {
            val imgPath = comboBox.getSelectionModel.getSelectedItem.toString match {
                case "Hut" => "file:images/Hat.jpg"
                case "Fingerhut" => "file:images/Fingerhut.jpg"
                case "Schubkarre" => "file:images/Schubkarre.jpg"
                case "Schuh" => "file:images/Schuh.jpg"
                case "Hund" => "file:images/Hund.jpg"
                case "Auto" => "file:images/Auto.jpg"
                case "Bügeleisen" => "file:images/Buegeleisen.jpg"
                case "Fingerhut" => "file:images/Fingerhut.jpg"
                case "Schiff" => "file:images/Schiff.jpg"
            }
            image.setImage(new Image(imgPath,
                200,
                200,
                true,
                true))
        }
        comboBox.value.onChange()

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(new Label("Name:"), 1, 0)
            add(tfPlayerName, 1, 1)
            add(image, 2, 0)
            add(comboBox, 1, 2)
        }

        // Enable/Disable login button depending on whether a username was entered.
        val startButton = dialog.dialogPane().lookupButton(startButtonType)
        startButton.disable = true

        // Do some validation (disable when username is empty).
        tfPlayerName.text.onChange { (_, _, newValue) => startButton.disable = newValue.trim().isEmpty }

        dialog.dialogPane().content = grid

        // Request focus on the username field by default.
        Platform.runLater(tfPlayerName.requestFocus())

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.resultConverter = dialogButton =>
            if (dialogButton == startButtonType) Result(tfPlayerName.text(), comboBox.getSelectionModel.getSelectedItem.toString)
            else null

        val result = dialog.showAndWait()

        result match {
            case Some(Result(name, figure)) => {
                controller.playerNames = controller.playerNames :+ name
                var imgPath = ""
                figure match {
                    case "Hut" => imgPath = "file:images/Hat.jpg"
                    case "Fingerhut" => imgPath = "file:images/Fingerhut.jpg"
                    case "Schubkarre" => imgPath = "file:images/Schubkarre.jpg"
                    case "Schuh" => imgPath = "file:images/Schuh.jpg"
                    case "Hund" => imgPath = "file:images/Hund.jpg"
                    case "Auto" => imgPath = "file:images/Auto.jpg"
                    case "Bügeleisen" => imgPath = "file:images/Buegeleisen.jpg"
                    case "Fingerhut" => imgPath = "file:images/Fingerhut.jpg"
                    case "Schiff" => imgPath = "file:images/Schiff.jpg"
                }
                // ausgewählte figur aus der auswahl nehmen
                controller.remainingFiguresToPick = controller.remainingFiguresToPick.filterNot(elm => elm == figure)
                controller.playerFigures = controller.playerFigures :+ imgPath
            }
            case None => "Dialog returned: None"
        }
    }

    def rollForPosDialog(e: OpenRollForPosDialogEvent): Unit = {
        new Alert(AlertType.Information) {
            initOwner(e.stage)
            title = "Roll for starting positions"
            headerText = "Player " + e.player.name
            contentText = "Roll dices!"
        }.showAndWait()
    }

    def rollDiceDialog(e: OpenRollDiceDialogEvent): Unit = {
        new Alert(AlertType.Information) {
            initOwner(e.stage)
            title = "Roll dice"
            headerText = "Player " + e.player.name
            contentText = "Roll dices!"
        }.showAndWait()
    }

    def rollDiceDialogNoMoreWorking(e: OpenRollDiceDialogEvent): (Int, Int, Boolean) = {
        case class Result(roll1: Int, roll2: Int, pasch: Boolean)
        // Create the custom dialog.
        val dialog = new Dialog[Result]() {
            initOwner(e.stage)
            title = "Roll Dice:"
            headerText = "Player " + e.player.name + " roll dice"
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }

        // Set the button types.
        val startButtonType = new ButtonType("Roll Dice")
        dialog.dialogPane().buttonTypes = Seq(startButtonType)
        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)

            add(new Label("Roll"), 0, 0)
        }

        dialog.dialogPane().content = grid

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.resultConverter = dialogButton =>
            if (dialogButton == startButtonType) {
                val (a, b, c) = controller.playerController.wuerfeln
                Result(a, b, c)
            } else Result(0, 0, false)
        val result = dialog.showAndWait()

        result match {
            case Some(Result(roll1, roll2, pasch)) => {
                (roll1, roll2, pasch)
            }
            case None => (0, 0, false)
        }
    }

    def informationDialog(e: OpenInformationDialogEvent): Unit = {
        new Alert(AlertType.Information) {
            initOwner(e.stage)
            title = "Information Dialog"
            headerText = "Look, an Information Dialog."
            contentText = "I have a great message for you!"
        }.showAndWait()
    }

    def confirmationDialog(e: OpenConfirmationDialogEvent): Unit = {
        val alert = new Alert(AlertType.Confirmation) {
            initOwner(e.stage)
            title = "Confirmation Dialog"
            headerText = "Look, a Confirmation Dialog."
            contentText = "Do you want to quit?"
        }

        val result = alert.showAndWait()

        result match {
            case Some(ButtonType.OK) => System.exit(0)
            case _ => "Cancel"
        }
    }

    def inJailDialog(e: OpenInJailDialogEvent): Unit = {
        // todo
        val alert = new Alert(AlertType.Confirmation) {
            initOwner(e.stage)
            title = "Confirmation Dialog"
            headerText = "Look, a Confirmation Dialog."
            contentText = "Do you want to quit?"
        }

        val result = alert.showAndWait()

        result match {
            case Some(ButtonType.OK) => System.exit(0)
            case _ => "Cancel"
        }
    }
}


