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

class Gui(controller: GameControllerInterface) extends Observer {
    controller.add(this)

    def getController: GameControllerInterface = controller

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
            case e: OpenNormalTurnDialogEvent => normalTurnDialog(e)
            case e: OpenPlayerFreeDialog => playerFreeDialog(e)
            case e: OpenBuyableFieldDialog => buyableFieldDialog(e)
            case e: OpenPayRentDialog => payRentDialog(e)
            case e: OpenPlayerEnteredGoDialog => goDialog(e)
            case e: openGoToJailDialog => goToJailDialog(e)
            case e: OpenVisitJailDialog => visitJailDialog(e)
            case e: OpenParkFreeDialog => parkFreeDialog(e)
            case e: OpenIncomeTaxDialog => incomeTaxDialog(e)
            case e: OpenLuxuaryTaxDialog => luxuaryTaxDialog(e)
            case e: openGoToJailPaschDialog => goToJailPaschDialog(e)
            case e: OpenChanceDialog => chanceDialog(e)
            case e: OpenCommunityChestDialog => communityChestDialog(e)
            case e: OpenPlayerPassedGoDialog => playerWentOverGoDialog
            case e: OpenPlayerDeptDialog => playerDeptDialog(e)

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
        lblDiceResult.setText(controller.players(controller.currentPlayer).name + " Rolled: " + e.roll1.toString + " " + e.roll2.toString + " paschCount: " + controller.paschCount)
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

    // todo moveplayerfiguer
    //    def movePlayerSmooveTimerGui(): Unit = {
    //        val len = fieldCoordsX.length
    //        var i = 0
    //        val timer: Timer = new Timer()
    //        timer.schedule(new TimerTask {
    //            override def run(): Unit = {
    //                println(i + 1)
    //                //movePlayerGui(fieldCoordsX(i), fieldCoordsY(i))
    //                i += 1
    //                if (i == len) timer.cancel()
    //                timer.purge()
    //            }
    //        }, 1000, 500)
    //
    //
    //    }

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
                            },
                            new MenuItem {
                                text = "Save game"
                                onAction = handle {
                                    controller.onSaveGame()
                                }
                            },
                            new MenuItem {
                                text = "Load game"
                                onAction = handle {
                                    controller.onLoadGame()
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
            scene = new Scene() {
                fill = Black
                root = new VBox() {
                    padding = Insets(10)


                    val pane = new StackPane()
                    pane.setId("stackpane")
                    val boardImage = new ImageView(new Image("file:images/BoardMonopolyDeluxe1992.png", 800, 800, false, true))
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
                    val box2 = new HBox()
                    box2.children = Seq(pane, box)
                    children = Seq(menubar, box2)
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
            headerText = controller.players(controller.currentPlayer).name + " entered " + e.field.name
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val buyButton = new ButtonType("Buy", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(buyButton, ButtonType.Cancel)

        val image = new ImageView(e.field.image)


        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid

        //todo if (players(currentPlayer).money >= field.price) {

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
            headerText = controller.players(controller.currentPlayer).name + " entered " + e.field.name
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val payButton = new ButtonType("Pay", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(payButton)

        val image = new ImageView(e.field.image)


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
                if (controller.players(controller.currentPlayer).money < 0)
                    controller.checkDepth(controller.players(controller.currentPlayer), e.field.owner)
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
        // TODO check if name is already in playernames !!
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

        // image of selected street
        var image = new ImageView(new Image("file:images/emptryImage.png"))
        val tradeButton = new ButtonType("Trade", ButtonData.OKDone)
        val sellButton = new ButtonType("Sell to bank", ButtonData.OKDone)
        val buyHomeButton = new ButtonType("Buy house", ButtonData.OKDone)
        val sellHomeButton = new ButtonType("Sell house", ButtonData.OKDone)
        val getMortgageButton = new ButtonType("get mortgage", ButtonData.OKDone)
        val payMortgageButton = new ButtonType("pay mortgage", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(tradeButton, sellButton, buyHomeButton, sellHomeButton, getMortgageButton, payMortgageButton, ButtonType.Cancel)
        // init buttons
        val btnTrade = dialog.dialogPane().lookupButton(tradeButton)
        val btnSell = dialog.dialogPane().lookupButton(sellButton)
        val btnBuyHome = dialog.dialogPane().lookupButton(buyHomeButton)
        val btnSellHome = dialog.dialogPane().lookupButton(sellHomeButton)
        val btnGetMortage = dialog.dialogPane().lookupButton(getMortgageButton)
        val btnPayMortage = dialog.dialogPane().lookupButton(payMortgageButton)
        //todo init buttons and streets and show mortgage , houses
        btnTrade.setVisible(false)
        btnSell.setVisible(false)
        btnBuyHome.setVisible(false)
        btnSellHome.setVisible(false)
        btnGetMortage.setVisible(false)
        btnPayMortage.setVisible(false)
        // trade button nur wenn spieler besitz haben und wenn spieller nicht aktueller spieler ist
        if (controller.players(controller.currentPlayer).name != controller.players(playerIdx).name) {
            if (controller.players(playerIdx).ownedStreets.nonEmpty) btnTrade.setVisible(true)
        }

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
                        //strasse holen
                        val street = controller.board.filter(_.name == cell.text.value)(0).asInstanceOf[Buyable]
                        // bild setzen je nachdem ob hypothek
                        if (street.mortgage) image.setImage(new Image("file:images/Mortgaged.png"))
                        else image.setImage(street.image)
                        // mortgage button
                        btnPayMortage.setVisible(false)
                        btnGetMortage.setVisible(false)
                        if (controller.players(controller.currentPlayer).name == controller.players(playerIdx).name) {
                            if (street.mortgage) btnPayMortage.setVisible(true)
                            else if (!street.mortgage) btnGetMortage.setVisible(true)
                        }


                        // sell street button
                        // wenn spieler aktueller spieler ist darf er verkaufen
                        if (controller.players(controller.currentPlayer).name == controller.players(playerIdx).name) {
                            btnSell.setVisible(true)
                            //todo btnBuyHome.setVisible(true) // nur wenn gruppe im besitz
                            //todo btnSellHome.setVisible(true) // nur wenn haus drauf
                            // if group = wasserwerk/ewerk/bahnhoefe hide houses and buttons

                        }
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

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.resultConverter = dialogButton =>
            if (dialogButton == tradeButton) Result("trade")
            else if (dialogButton == getMortgageButton) Result("getMortgage")
            else if (dialogButton == payMortgageButton) Result("payMortgage")
            else if (dialogButton == sellButton) Result("sell")
            else null

        val result = dialog.showAndWait()


        if (lvSelectedPlayer.getSelectionModel.getSelectedItem.nonEmpty) {
            val street = controller.board.filter(_.name == lvSelectedPlayer.getSelectionModel.getSelectedItem)(0).asInstanceOf[Buyable]

            result match {
                case Some(Result("trade")) => {
                    openTradeDialog(playerIdx)
                }
                case Some(Result("getMortgage")) => {
                    controller.board = controller.board.updated(controller.board.indexOf(street), street.getMortgage())
                    controller.players = controller.players.updated(playerIdx, controller.players(playerIdx).incMoney(100)) // todo .incMoney(street.mortgageValue))
                }
                case Some(Result("payMortgage")) => {
                    controller.board = controller.board.updated(controller.board.indexOf(street), street.payMortgage())
                    controller.players = controller.players.updated(playerIdx, controller.players(playerIdx).decMoney(100)) // todo .dec(street.mortgageValue))
                }
                case Some(Result("sell")) => {
                    controller.players = controller.players.updated(playerIdx, controller.players(playerIdx).sellStreet(controller.board.indexOf(street)))
                    controller.players = controller.players.updated(playerIdx, controller.players(playerIdx).incMoney(street.price))
                    controller.board = controller.board.updated(controller.board.indexOf(street), street.setOwner(-1))
                }
                case None => "Dialog returned: None"
            }
            updateListViewPlayers()
        }


    }


    def openTradeDialog(playerIdx: Int): Unit = {
        case class Result(option: String)

        // Create the custom dialog.
        val dialog = new Dialog[Result]() {
            title = "Tradewindow"
            headerText = controller.players(controller.currentPlayer).name + " and " + controller.players(playerIdx).name
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

        //set this to SINGLE to allow selecting just one item
        lvPlayer1.getSelectionModel().setSelectionMode(SelectionMode.Multiple)

        //set this to SINGLE to allow selecting just one item
        lvPlayer1.getSelectionModel().setSelectionMode(SelectionMode.Multiple)

        for (item <- controller.players(controller.currentPlayer).ownedStreets)
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
                // properties of player 1

                val streetP1 = controller.board.filter(_.name == lvPlayer1.getSelectionModel.getSelectedItem)(0).asInstanceOf[Buyable]

                // properties of player 2
                val streetP2 = controller.board.filter(_.name == lvPlayer2.getSelectionModel.getSelectedItem)(0).asInstanceOf[Buyable]

                //todo trade properties get list of selected properties for both players and set new owner for each lists
                //geld abziehen

                controller.players = controller.players.updated(controller.currentPlayer, controller.players(controller.currentPlayer).decMoney(tfPlayerXMoney.getText.toInt))
                controller.players = controller.players.updated(playerIdx, controller.players(playerIdx).decMoney(tfPlayerYMoney.getText.toInt))
                //geld draufzahlen
                controller.players = controller.players.updated(controller.currentPlayer, controller.players(controller.currentPlayer).incMoney(tfPlayerYMoney.getText.toInt))
                controller.players = controller.players.updated(playerIdx, controller.players(playerIdx).incMoney(tfPlayerXMoney.getText.toInt))
                print("trade items") // todo trade selected items and money
                // Markierte strassen von spieler 1 an spieler 2 geben
                controller.players = controller.players.updated(controller.currentPlayer, controller.players(controller.currentPlayer).sellStreet(controller.board.indexOf(streetP1))) // todo remove street
                controller.board = controller.board.updated(controller.board.indexOf(streetP1), streetP1.setOwner(playerIdx))
                controller.players = controller.players.updated(playerIdx, controller.players(playerIdx).buyStreet(controller.board.indexWhere(_.name == streetP1.name))) // todo addStreet
                // Markierte strassen von spieler 2 an spieler 1 geben
                controller.players = controller.players.updated(playerIdx, controller.players(playerIdx).sellStreet(controller.board.indexOf(streetP2)))
                controller.board = controller.board.updated(controller.board.indexOf(streetP2), streetP2.setOwner(controller.currentPlayer))
                controller.players = controller.players.updated(controller.currentPlayer, controller.players(controller.currentPlayer).buyStreet(controller.board.indexWhere(_.name == streetP2.name))) // todo addStreet

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

    def goToJailPaschDialog(e: openGoToJailPaschDialog): Unit = {
        // todo
        val alert = new Alert(AlertType.Confirmation) {
            title = "Go to jail"
            headerText = controller.paschCount + " Pasch gewuerfelt"
            contentText = "Ok"
        }

        val result = alert.showAndWait()

        result match {
            case Some(ButtonType.OK) => println("ok in jail dialog")
            case _ => "Cancel"
        }
    }

    // Dialogs which show up as player enters a field

    def goDialog(e: OpenPlayerEnteredGoDialog): Unit = {
        val dialog = new Dialog() {
            title = "Entered Go"
            headerText = controller.players(controller.currentPlayer).name + " entered " + e.field.name
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val payButton = new ButtonType("Collect 400$", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(payButton)

        val image = new ImageView(e.field.image)

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid
        dialog.showAndWait()
        controller.players = controller.players.updated(controller.currentPlayer, controller.players(controller.currentPlayer).incMoney(400))
    }

    def chanceDialog(e: OpenChanceDialog): Unit = {
        val dialog = new Dialog() {
            title = "Entered Chance"
            headerText = controller.players(controller.currentPlayer).name + " entered chance"
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val okButton = new ButtonType("Ok", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(okButton)

        //todo image = drawcard .. drawcard -> removecard from controller.cardstack if cardstackempty shuffle
        val image = new ImageView("file:images/AdvanceToGoChance.png")

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid
        dialog.showAndWait()
        //todo playercontroller.moveplayer
        print("chancefinished")
        //controller.players = controller.players.updated(controller.currentPlayer, controller.players(controller.currentPlayer).incMoney(400))
    }

    def communityChestDialog(e: OpenCommunityChestDialog): Unit = {
        val dialog = new Dialog() {
            title = "Entered Community Chest"
            headerText = controller.players(controller.currentPlayer).name + " entered Community chest"
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val okButton = new ButtonType("Ok", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(okButton)

        //todo image = drawcard .. drawcard -> removecard from controller.cardstack if cardstackempty shuffle
        val image = new ImageView("file:images/AdvanceToGoChest.png")

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid
        dialog.showAndWait()
        //todo playercontroller.moveplayer
        print("chestfinished")
        //controller.players = controller.players.updated(controller.currentPlayer, controller.players(controller.currentPlayer).incMoney(400))
    }

    def inJailDialog(e: OpenInJailDialogEvent): Unit = {
        val dialog = new Dialog() {
            title = "Player is in jail"
            headerText = controller.players(controller.currentPlayer).name + " is in jail."
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val rollButton = new ButtonType("Roll dice", ButtonData.OKDone)
        val payButton = new ButtonType("Pay", ButtonData.OKDone)
        val useCardButton = new ButtonType("Use card", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(rollButton, payButton, useCardButton)

        val image = new ImageView("file:images/InJail.png")

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid
        dialog.showAndWait()
        // todo result
    }

    def normalTurnDialog(e: OpenNormalTurnDialogEvent): Unit = {
        val dialog = new Dialog() {
            title = "New turn"
            headerText = "It is " + controller.players(controller.currentPlayer).name + "`s turn."
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val okButton = new ButtonType("Ok", ButtonData.OKDone)

        dialog.dialogPane().buttonTypes = Seq(okButton)

        val image = new ImageView(controller.players(controller.currentPlayer).figure.getImage)

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid
        dialog.showAndWait()
    }

    def playerDeptDialog(e: OpenPlayerDeptDialog): Unit = {
        val dialog = new Dialog() {
            title = "Player has dept"
            headerText = controller.players(controller.currentPlayer).name + " has dept."
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val okButton = new ButtonType("Ok", ButtonData.OKDone)

        dialog.dialogPane().buttonTypes = Seq(okButton)

        // todo val image = controller.players(controller.currentPlayer).figure

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            //todo add(image, 2, 0)
        }
        dialog.dialogPane().content = grid
        dialog.showAndWait()
        // todo result
    }

    def goToJailDialog(e: openGoToJailDialog): Unit = {
        val dialog = new Dialog() {
            title = "Entered Go to jail"
            headerText = controller.players(controller.currentPlayer).name + " entered " + e.field.name
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val okButton = new ButtonType("Go to jail", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(okButton)

        val image = new ImageView(e.field.image)

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid
        dialog.showAndWait()
    }

    def playerWentOverGoDialog(): Unit = {
        val dialog = new Dialog() {
            title = "Player passed Go"
            headerText = controller.players(controller.currentPlayer).name + " passed " + controller.board(0).name
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val payButton = new ButtonType("Collect 200$", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(payButton)

        val image = new ImageView(controller.board(0).asInstanceOf[Los].image)

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid
        dialog.showAndWait()
        controller.players = controller.players.updated(controller.currentPlayer, controller.players(controller.currentPlayer).incMoney(200))
    }

    def visitJailDialog(e: OpenVisitJailDialog): Unit = {
        val dialog = new Dialog() {
            title = "Entered Go"
            headerText = controller.players(controller.currentPlayer).name + " entered " + e.field.name
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val okButton = new ButtonType("Ok", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(okButton)

        val image = new ImageView(e.field.image)

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid
        dialog.showAndWait()
    }

    def parkFreeDialog(e: OpenParkFreeDialog): Unit = {
        val dialog = new Dialog() {
            title = "Entered Park free"
            headerText = controller.players(controller.currentPlayer).name + " entered " + e.field.name
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        var text = "Ok"
        if (controller.collectedTax > 0) text = "Collect " + controller.collectedTax

        val okButton = new ButtonType(text, ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(okButton)

        val image = new ImageView(e.field.image)

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid
        dialog.showAndWait()
        if (controller.collectedTax > 0) {
            controller.players = controller.players.updated(controller.currentPlayer, controller.players(controller.currentPlayer).incMoney(controller.collectedTax))
            controller.collectedTax = 0

        }
    }

    def luxuaryTaxDialog(e: OpenLuxuaryTaxDialog): Unit = {
        val dialog = new Dialog() {
            title = "Entered Luxuary tax"
            headerText = controller.players(controller.currentPlayer).name + " entered " + e.field.name
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val okButton = new ButtonType("Pay 75$", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(okButton)

        val image = new ImageView(e.field.image)

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid
        dialog.showAndWait()
        controller.players = controller.players.updated(controller.currentPlayer, controller.players(controller.currentPlayer).decMoney(75))
        controller.collectedTax += 75
    }

    def incomeTaxDialog(e: OpenIncomeTaxDialog): Unit = {
        case class Result(option: String)

        // Create the custom dialog.
        val dialog = new Dialog[Result]() {
            title = "Entered Income Tax"
            headerText = controller.players(controller.currentPlayer).name + " entered " + e.field.name
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val pay10PercentButton = new ButtonType("Pay 10 %", ButtonData.OKDone)
        val pay200Button = new ButtonType("Pay 200 $", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(pay10PercentButton, pay200Button)

        val image = new ImageView(e.field.image)

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid


        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.resultConverter = dialogButton =>
            if (dialogButton == pay10PercentButton) Result("pay10percent")
            else if (dialogButton == pay200Button) Result("pay200")
            else null

        val result = dialog.showAndWait()

        result match {
            case Some(Result("pay10percent")) => {
                val playerTax = (controller.players(controller.currentPlayer).money * 0.1).toInt
                controller.collectedTax += playerTax
                controller.players = controller.players.updated(controller.currentPlayer, controller.players(controller.currentPlayer).decMoney(playerTax))
            }
            case Some(Result("pay200")) => {
                controller.collectedTax += 200
                controller.players = controller.players.updated(controller.currentPlayer, controller.players(controller.currentPlayer).decMoney(200))
            }
            case None => "Dialog returned: None"
        }
    }
}


