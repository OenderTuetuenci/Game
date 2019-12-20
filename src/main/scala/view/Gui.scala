package view

import controller._
import model._
import scalafx.Includes.{handle, _}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{Insets, Orientation, Pos}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout._
import scalafx.scene.paint.Color.{Black, PaleGreen, SeaGreen}
import scalafx.scene.paint.{Color, LinearGradient, Stops}
import scalafx.scene.text.Text
import scalafx.scene.{Cursor, Scene}
import util.Observer

import scala.io.StdIn._
import scala.language.implicitConversions

class Gui(controller: GameController) extends Observer {
    controller.add(this)

    def getController: GameController = controller

    override def update(e: PrintEvent): Any = {
        e match {
            //windows, dialogs
            case e: openGameOverDialogEvent => gameOverDialog(e)
            case e: OpenMainWindowEvent => mainWindow(e)
            case e: OpenGetPlayersDialogEvent => getPlayersDialog(e)
            case e: OpenGetNameDialogEvent => getPlayerNameDialog(e)
            case e: OpenRollDiceDialogEvent => rollDiceDialog(e)
            case e: OpenRollForPosDialogEvent => rollForPosDialog(e)
            case e: OpenInformationDialogEvent => informationDialog(e)
            case e: OpenConfirmationDialogEvent => confirmationDialog(e)
            case e: OpenInJailDialogEvent => inJailDialog(e)
            case e: OpenPlayerFreeDialog => playerFreeDialog(e)
            case e: OpenBuyableFieldDialog => buyableFieldDialog(e)
            case e: OpenPayRentDialog => payRentDialog(e)
            case e: openGoToJailDialog => goToJailDialog(e)
            // others
            case e: MovePlayerFigureEvent => movePlayerFigure(e)
            case e: ClearGuiElementsEvent => clearGuiElements
            case e: UpdateListViewPlayersEvent => updateListViewPlayers()
            case e: PlacePlayersOnBoardEvent => placePlayersOnBoard()
            case e: UpdateGuiDiceLabelEvent => updateGuiDiceLabel(e)
            case _ =>



            //Input
            case e: askUndoGetPlayersEvent => {
                println("Undo?")
                controller.answer = readLine()
            }

        }
        updateListViewEventLog(e.toString)
    }

    def updateGuiDiceLabel(e: UpdateGuiDiceLabelEvent) = {
        val lblDiceResult = controller.currentStage.scene().lookup("#lblDiceResult").asInstanceOf[javafx.scene.text.Text]
        lblDiceResult.setText(controller.players(controller.isturn).name + " Rolled: " + e.roll1.toString + " " + e.roll2.toString + " paschCount: " + controller.paschCount)
    }

    def placePlayersOnBoard() = {

    }

    def updateListViewPlayers() = {
        val listviewSpieler = controller.currentStage.scene().lookup("#lvPlayers").asInstanceOf[javafx.scene.control.ListView[String]]
        listviewSpieler.getItems.clear()
        for (player <- controller.players)
            listviewSpieler.getItems.add(player.toString)
    }

    def updateListViewEventLog(str: String) = {
        val listviewEventLog = controller.currentStage.scene().lookup("#lvEventLog").asInstanceOf[javafx.scene.control.ListView[String]]
        listviewEventLog.getItems.add(str)
        listviewEventLog.scrollTo(listviewEventLog.getItems.length)
    }

    def clearGuiElements() = {
        val listviewPlayers = controller.currentStage.scene().lookup("#lvPlayers").asInstanceOf[javafx.scene.control.ListView[String]]
        listviewPlayers.getItems.clear
        val listviewEventLog = controller.currentStage.scene().lookup("#lvEventLog").asInstanceOf[javafx.scene.control.ListView[String]]
        listviewEventLog.getItems.clear
    }

    def movePlayerFigure(e: MovePlayerFigureEvent) = {
        println("moveplayer x y " + e.x + e.y)
        e.playerFigure.setTranslateX(e.x)
        e.playerFigure.setTranslateY(e.y)
    }

    // widgets

    def mainWindow(e: OpenMainWindowEvent) = {
        controller.currentStage = new PrimaryStage {
            val menubar = new MenuBar {
                menus = List(
                    new Menu("Game") {
                        items = List(
                            new MenuItem {
                                text = "Start new game"
                                onAction = handle {
                                    controller.onStartGame()
                                }
                            }
                        )
                    },
                    new Menu("Settings") {
                        items = List(
                            new MenuItem("Resolution"),
                            new MenuItem("Etc"),
                        )
                    },
                    new Menu("Help") {
                        items = List(
                            new MenuItem {
                                text = "About"
                                onAction = handle {
                                    controller.onInformation()
                                }
                            }
                        )
                    },
                    new Menu("Quit") {
                        items = List(
                            new MenuItem {
                                text = "Quit"
                                onAction = handle {
                                    controller.onQuit()
                                }
                            }
                        )
                    }
                )
            }
            menubar.setId("menubar")
            title = "Monopoly SE"
            scene = new Scene(1100, 800) {
                fill = Black
                content = new HBox(menubar) {
                    padding = Insets(10)
                    val pane = new StackPane()
                    pane.setId("stackpane")
                    val boardImage = new ImageView(new Image("file:images/board.jpg", 800, 800, false, true))
                    val box = new VBox(
                        new Text {
                            id = "lblPlayerTurn"
                            text = "Game Over"
                            style = "-fx-font-size: 20pt"
                            fill = new LinearGradient(
                                endX = 0,
                                stops = Stops(PaleGreen, SeaGreen))
                        },
                        new Button {
                            text = "Roll Dice"
                            id = "rollDice"
                            onAction = handle {
                                controller.onRollDice
                            }
                            alignmentInParent = Pos.Center
                            hgrow = Priority.Always
                            maxWidth = Double.MaxValue
                            padding = Insets(7)
                            this.setDisable(true)
                        },
                        new Text {
                            id = "lblDiceResult"
                            text = "Result roll dice"
                            style = "-fx-font-size: 20pt"
                            fill = new LinearGradient(
                                endX = 0,
                                stops = Stops(PaleGreen, SeaGreen))
                        },
                        new Button {
                            text = "End turn"
                            id = "endTurn"
                            onAction = handle {
                                controller.onEndTurn
                            }
                            alignmentInParent = Pos.Center
                            hgrow = Priority.Always
                            maxWidth = Double.MaxValue
                            padding = Insets(7)
                            this.setDisable(true)
                        },
                        new Text {
                            text = "Players                                                                          "
                            style = "-fx-font-size: 20pt"
                            fill = new LinearGradient(
                                endX = 0,
                                stops = Stops(PaleGreen, SeaGreen))
                        },
                        // todo listview[player]
                        //  onclick expand view to see streets of 1 player......
                        new ListView[String] {
                            this.setId("lvPlayers")
                            orientation = Orientation.Vertical

                            cellFactory = {

                                p => {

                                    val cell = new ListCell[String]

                                    cell.textFill = Color.Blue

                                    cell.cursor = Cursor.Hand

                                    cell.item.onChange { (_, _, str) => cell.text = str }

                                    cell.onMouseClicked = { me: MouseEvent => {
                                        playerStatsDialog(cell.index.toInt)
                                        updateListViewPlayers()
                                    }
                                    }

                                    cell

                                }

                            }

                            items = ObservableBuffer()

                        },
                        new Text {
                            text = "Event Log"
                            style = "-fx-font-size: 20pt"
                            fill = new LinearGradient(
                                endX = 0,
                                stops = Stops(PaleGreen, SeaGreen))
                        },
                        // Event Log über tui
                        new ListView[String] {
                            this.setId("lvEventLog")
                            this.getSelectionModel.setSelectionMode(SelectionMode.Multiple)
                            orientation = Orientation.Vertical

                            cellFactory = {

                                p => {

                                    val cell = new ListCell[String]

                                    cell.textFill = Color.Blue

                                    cell.cursor = Cursor.Hand

                                    cell.item.onChange { (_, _, str) => cell.text = str }

                                    cell.onMouseClicked = { me: MouseEvent =>
                                        println("Do something with " + cell.text.value)
                                        //cell.updateSelected(false)
                                        //if (cell.isSelected) this.getSelectionModel.clearSelection
                                    }

                                    cell

                                }

                            }

                            items = ObservableBuffer()

                        }
                    )
                    pane.children = List(boardImage)
                    children = Seq(menubar, pane, box)
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

    //    def playerListItem(imgPath: String="",name: String="",money: String="") = new VBox() { // todo in die playerlist später
    //        ;
    //    }

    def button[R](text: String, action: () => R, idString: String = "") = new Button(text) {
        onAction = handle {
            action()
        }
        alignmentInParent = Pos.Center
        hgrow = Priority.Always
        maxWidth = Double.MaxValue
        padding = Insets(7)
        this.setId("idString")
    }
    // Dialogs

    def getPlayersDialog(e: OpenGetPlayersDialogEvent) = {

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
            text = "0"
        }
        val tfNpcCount = new TextField() {
            promptText = "npcCount"
            text = "0"
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
        //tfPlayerCount.text.onChange { (_, _, newValue) =>
        //    startButton.disable = tfPlayerCount.text().toInt + tfNpcCount.text().toInt <= 8 && newValue.trim().isEmpty}


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
            }
            case None => ("Dialog returned", "None") //todo initstate
        }
    }

    def buyableFieldDialog(e: OpenBuyableFieldDialog): Unit = {
        case class Result(option: String)

        // Create the custom dialog.
        val dialog = new Dialog[Result]() {
            title = "Entered buyable"
            headerText = controller.players(controller.isturn).name + " entered " + e.field.name
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val buyButton = new ButtonType("Buy", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(buyButton, ButtonType.Cancel)

        val image = new ImageView(new Image("file:images/broadwalk.png"))
        //            ,
        //            400,
        //            400,
        //            true,
        //            true))


        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid

        //todo if (players(isturn).money >= field.price) {

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.resultConverter = dialogButton =>
            if (dialogButton == buyButton) Result("buy")
            else null

        val result = dialog.showAndWait()

        result match {
            case Some(Result("buy")) => {
                controller.buy
            }
            case None => "Dialog returned: None"
        }
    }

    def payRentDialog(e: OpenPayRentDialog): Unit = {
        case class Result(option: String)

        // Create the custom dialog.
        val dialog = new Dialog[Result]() {
            title = "Entered owned Field"
            headerText = controller.players(controller.isturn).name + " entered " + e.field.name
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val payButton = new ButtonType("Pay", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(payButton, ButtonType.Cancel)

        val image = new ImageView(new Image("file:images/broadwalk.png"))
        //            ,
        //            400,
        //            400,
        //            true,
        //            true))


        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid


        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.resultConverter = dialogButton =>
            if (dialogButton == payButton) Result("pay")
            else null

        val result = dialog.showAndWait()

        result match {
            case Some(Result("pay")) => {
                controller.payRent
            }
            case None => "Dialog returned: None"
        }
    }

    def getPlayerNameDialog(e: OpenGetNameDialogEvent) = {
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
            case "Auto" => "file:images/Auto.png"
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
                case "Auto" => "file:images/Auto.png"
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
                val imgPath = figure match {
                    case "Hut" => "file:images/Hat.jpg"
                    case "Fingerhut" => "file:images/Fingerhut.jpg"
                    case "Schubkarre" => "file:images/Schubkarre.jpg"
                    case "Schuh" => "file:images/Schuh.jpg"
                    case "Hund" => "file:images/Hund.jpg"
                    case "Auto" => "file:images/Auto.png"
                    case "Bügeleisen" => "file:images/Buegeleisen.jpg"
                    case "Fingerhut" => "file:images/Fingerhut.jpg"
                    case "Schiff" => "file:images/Schiff.jpg"
                }
                // ausgewählte figur aus der auswahl nehmen
                controller.remainingFiguresToPick = controller.remainingFiguresToPick.filterNot(elm => elm == figure)
                controller.playerFigures = controller.playerFigures :+ imgPath
            }
            case None => "Dialog returned: None"
        }
    }

    def playerStatsDialog(playerIdx: Int): Unit = {
        //todo buy house sell street vlt hier rein erstmal
        case class Result(option: String)

        // Create the custom dialog.
        val dialog = new Dialog[Result]() {
            title = controller.players(playerIdx).name
            headerText = "Stats"
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val tradeButton = new ButtonType("Trade", ButtonData.OKDone)
        val sellButton = new ButtonType("Sell to bank", ButtonData.OKDone)
        val buyHomeButton = new ButtonType("Buy house / hotel", ButtonData.OKDone)
        val sellHomeButton = new ButtonType("Buy house / hotel", ButtonData.OKDone)
        val getOrPayMortgage = new ButtonType("get / pay mortgage", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(tradeButton, sellButton, buyHomeButton, sellHomeButton, getOrPayMortgage, ButtonType.Cancel)

        //todo init buttons and streets and show mortgage , houses
        // if group = wasserwerk/ewerk/bahnhoefe hide houses and buttons

        // image of street
        var image = new ImageView(new Image("file:images/broadwalk.png"))
        // Properties of selected player
        val lvSelectedPlayer = new ListView[String] {
            this.setId("lvSelectedPlayer")
            orientation = Orientation.Vertical
            cellFactory = {
                p => {
                    val cell = new ListCell[String]
                    cell.textFill = Color.Blue
                    cell.cursor = Cursor.Hand
                    cell.item.onChange { (_, _, str) => cell.text = str }
                    cell.onMouseClicked = { me: MouseEvent =>
                        // todo image = street.image
                        //if (controller.board())
                        println("Do something with " + cell.text.value)

                    }
                    cell
                }
            }
            items = ObservableBuffer()
        }
        for (item <- controller.players(playerIdx).ownedStreets)
            lvSelectedPlayer.getItems.add(controller.board(item).name)
        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(lvSelectedPlayer, 0, 0)
            add(image, 1, 0)
        }
        dialog.dialogPane().content = grid

        val btn = dialog.dialogPane().lookupButton(tradeButton)

        if (controller.players(controller.isturn).name == controller.players(playerIdx).name) btn.setVisible(false)

        btn.disable

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.resultConverter = dialogButton =>
            if (dialogButton == tradeButton) Result("trade")
            else null

        val result = dialog.showAndWait()

        result match {
            case Some(Result("trade")) => {
                openTradeDialog(playerIdx) // todo player currentturn and player to trade
            }
            case None => "Dialog returned: None"
        }
    }

    def openTradeDialog(playerIdx: Int): Unit = {
        case class Result(option: String)

        // Create the custom dialog.
        val dialog = new Dialog[Result]() {
            title = "Tradewindow"
            headerText = controller.players(controller.isturn).name + " and " + controller.players(playerIdx).name
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val tradeButton = new ButtonType("accept", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(tradeButton, ButtonType.Cancel)
        val tfPlayerXMoney = new TextField() {
            promptText = "MoneyPlayerX"
        }
        val tfPlayerYMoney = new TextField() {
            promptText = "MoneyPlayerY"
        }
        // Properties of Player x
        val lvPlayer1 = new ListView[String] {
            this.setId("lvPlayerX")
            orientation = Orientation.Vertical
            cellFactory = {
                p => {
                    val cell = new ListCell[String]
                    cell.textFill = Color.Blue
                    cell.cursor = Cursor.Hand
                    cell.item.onChange { (_, _, str) => cell.text = str }
                    cell.onMouseClicked = { me: MouseEvent => println("Do something with " + cell.text.value) }
                    cell
                }
            }
            items = ObservableBuffer()
        }
        // Properties of Player y
        val lvPlayer2 = new ListView[String] {
            this.setId("lvPlayerY")
            orientation = Orientation.Vertical
            cellFactory = {
                p => {
                    val cell = new ListCell[String]
                    cell.textFill = Color.Blue
                    cell.cursor = Cursor.Hand
                    cell.item.onChange { (_, _, str) => cell.text = str }
                    cell.onMouseClicked = { me: MouseEvent =>
                        println("Do something with " + cell.text.value)
                        cell.selected = false
                    }
                    cell
                }
            }
            items = ObservableBuffer()
        }
        for (item <- controller.players(controller.isturn).ownedStreets)
            lvPlayer1.getItems.add(controller.board(item).name)
        for (item <- controller.players(playerIdx).ownedStreets)
            lvPlayer2.getItems.add(controller.board(item).name)

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(lvPlayer1, 0, 0)
            add(lvPlayer2, 1, 0)
            add(tfPlayerXMoney, 0, 1)
            add(tfPlayerYMoney, 1, 1)

        }
        dialog.dialogPane().content = grid

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.resultConverter = dialogButton =>
            if (dialogButton == tradeButton) Result("accept")
            else null

        val result = dialog.showAndWait()

        result match {
            case Some(Result("accept")) => {
                //todo trade properties get list of selected properties for both players and set new owner for each lists
                //geld abziehen
                controller.players = controller.players.updated(controller.isturn, controller.players(controller.isturn).decMoney(tfPlayerXMoney.getText.toInt))
                controller.players = controller.players.updated(playerIdx, controller.players(playerIdx).decMoney(tfPlayerYMoney.getText.toInt))
                //geld draufzahlen
                controller.players = controller.players.updated(controller.isturn, controller.players(controller.isturn).incMoney(tfPlayerYMoney.getText.toInt))
                controller.players = controller.players.updated(playerIdx, controller.players(playerIdx).incMoney(tfPlayerXMoney.getText.toInt))
                print("trade items") // todo trade selected items and money
            }
            case None => "Dialog returned: None"
        }
    }


    def rollForPosDialog(e: OpenRollForPosDialogEvent): Unit = {
        new Alert(AlertType.Information) {
            title = "Roll for starting positions"
            headerText = "Player " + e.player.name
            contentText = "Roll dices!"
        }.showAndWait()
    }

    def rollDiceDialog(e: OpenRollDiceDialogEvent): Unit = {
        new Alert(AlertType.Information) {
            title = "Roll dice"
            headerText = "Player " + e.player.name
            contentText = "Roll dices!"
        }.showAndWait()
    }

    def informationDialog(e: OpenInformationDialogEvent): Unit = {
        new Alert(AlertType.Information) {
            title = "Information Dialog"
            headerText = "Look, an Information Dialog."
            contentText = "I have a great message for you!"
        }.showAndWait()
    }

    def confirmationDialog(e: OpenConfirmationDialogEvent): Unit = {
        val alert = new Alert(AlertType.Confirmation) {
            title = "Confirmation Dialog"
            headerText = "Do u want to quit?"
        }

        val result = alert.showAndWait()

        result match {
            case Some(ButtonType.OK) => System.exit(0)
            case _ => "Cancel"
        }
    }

    def gameOverDialog(e: openGameOverDialogEvent): Unit = {
        val alert = new Alert(AlertType.Confirmation) {
            title = "Game Over"
            headerText = "Winner is"
            contentText = "todo winner"
        }

        alert.showAndWait()
    }

    def inJailDialog(e: OpenInJailDialogEvent): Unit = {
        // todo
        val alert = new Alert(AlertType.Confirmation) {
            title = "Player is in jail"
            headerText = "Look, a Confirmation Dialog."
            contentText = "Ok"
        }

        val result = alert.showAndWait()

        result match {
            case Some(ButtonType.OK) => println("ok in jail dialog")
            case _ => "Cancel"
        }
    }

    def playerFreeDialog(e: OpenPlayerFreeDialog): Unit = {
        // todo
        val alert = new Alert(AlertType.Confirmation) {
            title = "Player is free"
            headerText = "Look, a Confirmation Dialog."
            contentText = "Ok"
        }

        val result = alert.showAndWait()

        result match {
            case Some(ButtonType.OK) => println("ok in jail dialog")
            case _ => "Cancel"
        }
    }


    def goToJailDialog(e: openGoToJailDialog): Unit = {
        // todo
        val alert = new Alert(AlertType.Confirmation) {
            title = "Go to jail"
            headerText = "Look, a Confirmation Dialog."
            contentText = "Ok"
        }

        val result = alert.showAndWait()

        result match {
            case Some(ButtonType.OK) => println("ok in jail dialog")
            case _ => "Cancel"
        }
    }
}


