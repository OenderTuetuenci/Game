package view

import controller.controllerComponent.ControllerInterface
import controller.controllerComponent.controllerBaseImpl.getPlayernameAndFigureCommand
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
import scalafx.stage.StageStyle
import util.Observer

import scala.language.implicitConversions

class Gui(controller: ControllerInterface) extends Observer {
    controller.add(this)

    def getController: ControllerInterface = controller

    override def update(e: Event): Any = {
        e match {
            //windows, dialogs
            case e: openGameOverDialogEvent => gameOverDialog(e)
            case e: OpenMainWindowEvent => mainWindow(e)
            case e: OpenGetPlayersDialogEvent => getPlayersDialog(e)
            case e: OpenGetNameDialogEvent => getPlayerNameDialog(e)
            case e: OpenRollForPosDialogEvent => rollForPosDialog(e)
            case e: OpenInformationDialogEvent => informationDialog(e)
            case e: OpenAuctionDialogEvent => auctionDialog(e)
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
            case e: UpdateListViewPlayersEvent => updateListViewPlayers()
            case e: UpdateListViewEventLogEvent => updateListViewEventLog(e)
            case e: UpdateGuiDiceLabelEvent => updateGuiDiceLabel(e)
            case _ =>

        }
    }

    def updateGuiDiceLabel(e: UpdateGuiDiceLabelEvent) = {
        val lblDiceResult = controller.currentStage.scene().lookup("#lblDiceResult").asInstanceOf[javafx.scene.text.Text]
        lblDiceResult.setText(controller.players(controller.currentPlayer).name + " Rolled: " + e.roll1.toString + " " + e.roll2.toString + " paschCount: " + controller.paschCount)
    }

    def updateListViewPlayers() = {
        val listviewSpieler = controller.currentStage.scene().lookup("#lvPlayers").asInstanceOf[javafx.scene.control.ListView[String]]
        listviewSpieler.getItems.clear()
        for (player <- controller.players)
            listviewSpieler.getItems.add(player.name + " " + " Money: " + player.money)
    }

    def updateListViewEventLog(e: UpdateListViewEventLogEvent) = {
        val listviewEventLog = controller.currentStage.scene().lookup("#lvEventLog").asInstanceOf[javafx.scene.control.ListView[String]]
        listviewEventLog.getItems.add(e.str)
        listviewEventLog.scrollTo(listviewEventLog.getItems.length)
    }

    def movePlayerFigure(e: MovePlayerFigureEvent) = {
        val stackpane = controller.currentStage.scene().lookup("#stackpane").asInstanceOf[javafx.scene.layout.StackPane]
        // todo get scale and board position
        //val vboxStackpane = stackpane.getChildren().filtered(_.getId == "#vboxStackpane").get(0)
        //print("bounds:" + vboxStackpane)
        val figure = stackpane.getChildren().filtered(_.getId == "#player" + controller.currentPlayer)
        figure.get(0).setTranslateX(e.x)
        figure.get(0).setTranslateY(e.y)
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
                                this.setId("miSaveGame")
                                //this.setDisable(true)
                            },
                            new MenuItem {
                                text = "Load game"
                                onAction = handle {
                                    controller.onLoadGame()
                                }
                                //this.setDisable(true) //todo weg spaeter wenn persistent
                                this.setId("miLoadGame")
                            }

                        )
                        this.setId("menuGame")
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
                    val pane = new StackPane {
                        this.setId("stackpane")
                    }
                    val boardImage = new ImageView(new Image("file:images/BoardMonopolyDeluxe1992.png", 800, 800, false, true))
                    boardImage.setId("#boardImage")
                    val vbox = new VBox(
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
                        pane
                    )
                    vbox.setId("#vboxStackpane")
                    val box = new VBox(
                        new Text {
                            text = "Players"
                            style = "-fx-font-size: 20pt"
                            fill = new LinearGradient(
                                endX = 0,
                                stops = Stops(PaleGreen, SeaGreen))
                        },
                        // todo listview[player]
                        // todo onclick expand view to see streets of 1 player......
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
                    pane.children = boardImage
                    val box2 = new HBox()
                    box2.children = Seq(vbox, box)
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
        controller.currentStage.initStyle(StageStyle.Undecorated)

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
        val image = new ImageView(new Image("file:images/emptryImage.png"))
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
            if (controller.board.filter(x => x.group != 0 && x.asInstanceOf[Buyable].owner == playerIdx).nonEmpty) btnTrade.setVisible(true)
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
                        else image.setImage(new Image(street.image))
                        // mortgage button
                        btnPayMortage.setVisible(false)
                        btnGetMortage.setVisible(false)
                        if (controller.players(controller.currentPlayer).name == controller.players(playerIdx).name) {
                            if (street.mortgage) btnPayMortage.setVisible(true)
                            else if (!street.mortgage) btnGetMortage.setVisible(true)
                        }
                        // wenn spieler aktueller spieler ist darf er verkaufen
                        if (controller.players(controller.currentPlayer).name == controller.players(playerIdx).name) {
                            btnSell.setVisible(true)
                            // wenn spieler alle strassen der gruppe besitzt darf er ein haus kaufen
                            // wenn 4 haeuser dann hotel
                            // nur wenn strasse nicht hypothek
                            // und wenn haeuser <5
                            // kein bahnhof kein ewerk, wasserwerk
                            btnBuyHome.setVisible(true)
                            for (street <- controller.board.filter(_.group == street.group)) {
                                if (street.asInstanceOf[Buyable].owner != controller.currentPlayer) {
                                    //                                    || controller.players(controller.currentPlayer).money < 200
                                    //                                    || street.asInstanceOf[Buyable].mortgage
                                    //                                    || street.asInstanceOf[Buyable].homecount == 5
                                    //                                    || street.asInstanceOf[Buyable].group == 1
                                    //                                    || street.asInstanceOf[Buyable].group == 2

                                    btnBuyHome.setVisible(false)
                                }
                            }

                            //todo btnSellHome.setVisible(true) // nur wenn haus drauf
                            // if group = wasserwerk/ewerk/bahnhoefe hide houses and buttons

                        }
                    }
                    cell
                }
            }
            items = ObservableBuffer()
        }

        for (item <- controller.board.filter(x => x.group != 0 && x.asInstanceOf[Buyable].owner == playerIdx))
            lvSelectedPlayer.getItems.add(item.name)
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

        dialog.initStyle(StageStyle.Undecorated)
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
                case Some(Result("sell")) =>
                    controller.players = controller.players.updated(playerIdx, controller.players(playerIdx).incMoney(street.price))
                    controller.board = controller.board.updated(controller.board.indexOf(street), street.setOwner(-1))

                case None => "Dialog returned: None"
            }
            // falls spieler schulden hat und etwas verkauft wird geschaut ob er wieder im plus ist
            controller.checkPlayerDept(-1) //
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
            text = "0"
        }
        val tfPlayerYMoney = new TextField() {
            promptText = "MoneyPlayerY"
            text = "0"
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

        for (item <- controller.board.filter(x => x.group != 0 && x.asInstanceOf[Buyable].owner == controller.currentPlayer))
            lvPlayer1.getItems.add(item.name)
        for (item <- controller.board.filter(x => x.group != 0 && x.asInstanceOf[Buyable].owner == playerIdx))
            lvPlayer2.getItems.add(item.name)

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

        // Convert the result
        dialog.resultConverter = dialogButton =>
            if (dialogButton == tradeButton) Result("accept")
            else null
        dialog.initStyle(StageStyle.Undecorated)
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
                controller.board = controller.board.updated(controller.board.indexOf(streetP1), streetP1.setOwner(playerIdx))
                // Markierte strassen von spieler 2 an spieler 1 geben
                controller.board = controller.board.updated(controller.board.indexOf(streetP2), streetP2.setOwner(controller.currentPlayer))
                controller.checkPlayerDept(-1)
            }
            case None => "Dialog returned: None"
        }
    }

    def getPlayersDialog(e: OpenGetPlayersDialogEvent) = {

        case class Result(playerCount: String, npcCount: String)

        val dialog = new Dialog[Result]() {
            title = "Start Game"
            headerText = "How many players and npc"
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }

        val okButtonType = new ButtonType("Start", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)

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

        val okButton = dialog.dialogPane().lookupButton(okButtonType)
        okButton.disable = true

        tfPlayerCount.text.onChange {
            okButton.disable = tfPlayerCount.text().toInt + tfNpcCount.text().toInt >= 8
        }

        tfNpcCount.text.onChange {
            okButton.disable = tfPlayerCount.text().toInt + tfNpcCount.text().toInt >= 8
        }

        dialog.dialogPane().content = grid
        // Request focus on the username field by default.
        Platform.runLater(tfPlayerCount.requestFocus())
        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.resultConverter = dialogButton =>
            if (dialogButton == okButtonType) Result(tfPlayerCount.text(), tfNpcCount.text())
            else null

        dialog.initStyle(StageStyle.Undecorated)
        val result = dialog.showAndWait()

        result match {
            case Some(Result(p, npc)) => {
                controller.humanPlayers = p.toInt
                controller.npcPlayers = npc.toInt
                controller.runNewGame()
            }
            case None => ("Dialog returned", "None")
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
        val auctionButton = new ButtonType("Auction", ButtonData.OKDone)

        dialog.dialogPane().buttonTypes = Seq(buyButton, auctionButton)

        val image = new ImageView(new Image(e.field.image))


        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid

        // validation for buy button
        val btnBuy = dialog.dialogPane().lookupButton(buyButton)
        btnBuy.disable = controller.players(controller.currentPlayer).money < e.field.price

        // convert result
        dialog.resultConverter = dialogButton =>
            if (dialogButton == buyButton) Result("buy")
            else if (dialogButton == auctionButton) Result("auction")
            else Result("auction")

        dialog.initStyle(StageStyle.Undecorated)
        val result = dialog.showAndWait()

        result match {
            case Some(Result("buy")) =>
                controller.buy(e.field)
            case Some(Result("auction")) =>
                controller.notifyObservers(AuctionStartedEventTui(e.field))
                controller.notifyObservers(OpenAuctionDialogEvent(e.field))
            case None => "Dialog returned: None"
        }
    }

    def payRentDialog(e: OpenPayRentDialog): Unit = {
        val dialog = new Dialog() {
            title = "Entered owned Field"
            headerText = controller.players(controller.currentPlayer).name + " entered " + e.field.name
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        //dialog.getDialogPane.setPrefSize(600, 500)
        val payButton = new ButtonType("Pay", ButtonData.OKDone)
        dialog.dialogPane().buttonTypes = Seq(payButton)

        val image = new ImageView(new Image(e.field.image))

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid

        dialog.initStyle(StageStyle.Undecorated)
        dialog.showAndWait()

        controller.payRent(e.field)
        controller.checkPlayerDept(e.field.owner)
    }

    def getPlayerNameDialog(e: OpenGetNameDialogEvent) = {
        case class Result(playerName: String, figure: String)

        val dialog = new Dialog[Result]() {
            title = "Enter Player names:"
            headerText = "Player " + (e.currPlayer + 1) + " enter name"
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        dialog.getDialogPane.setPrefSize(250, 400)
        val buttonNextType = new ButtonType("Next", ButtonData.OKDone)
        val buttonBackType = new ButtonType("Back", ButtonData.OKDone)

        dialog.dialogPane().buttonTypes = Seq(buttonNextType, buttonBackType)

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

        val hbox = new HBox(
            new Label("Name:"),
            tfPlayerName
        )
        hbox.padding = Insets(10, 10, 10, 10)

        val hbox1 = new HBox(
            new Label("Figure:"),
            comboBox
        )
        hbox1.padding = Insets(10, 10, 10, 10)

        val layoutBox = new VBox(
            hbox,
            hbox1,
            image,
        )

        layoutBox.padding = Insets(10, 10, 10, 10)

        dialog.dialogPane().content = layoutBox

        // Enable/Disable login button depending on whether a username was entered.
        val nextButton = dialog.dialogPane().lookupButton(buttonNextType)
        val backButton = dialog.dialogPane().lookupButton(buttonBackType)
        nextButton.disable = true

        // Do some validation (disable when username is empty).
        // TODO check if name is already in playernames !!
        tfPlayerName.text.onChange {
            nextButton.disable = tfPlayerName.text == "" || controller.playerNames.contains(tfPlayerName.text)
        }

        // init buttons
        if (e.currPlayer == controller.humanPlayers - 1) nextButton.setAccessibleText("Start game")
        else nextButton.setAccessibleText("Next player")

        if (e.currPlayer > 0) backButton.setVisible(true)
        else backButton.setVisible(false)

        dialog.resultConverter = dialogButton =>
            if (dialogButton == buttonNextType) Result(tfPlayerName.text(), comboBox.getSelectionModel.getSelectedItem.toString)
            else if (dialogButton == buttonBackType) Result("Back", "Back")
            else null
        // Request focus on the username field by default.
        Platform.runLater(tfPlayerName.requestFocus())

        dialog.initStyle(StageStyle.Undecorated)
        val result = dialog.showAndWait()

        result match {
            case Some(Result("Back", "Back")) => controller.undoManager.undoStep
            case Some(Result(name, figure)) => {
                controller.undoManager.doStep(new getPlayernameAndFigureCommand(controller, controller.currentPlayer,
                    controller.playerNames, controller.playerFigures, controller.remainingFiguresToPick))
                controller.playerNames = controller.playerNames :+ name
                val imgPath = figure match {
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
                // ausgewählte figur aus der auswahl nehmen
                controller.remainingFiguresToPick = controller.remainingFiguresToPick.filterNot(elm => elm == figure)
                controller.playerFigures = controller.playerFigures :+ imgPath

            }
            case None => "Dialog returned: None"
        }
    }


    def auctionDialog(e: OpenAuctionDialogEvent): Unit = {
        case class Result(option: String)

        val dialog = new Dialog[Result]() {
            title = "Auction"
            headerText = "Auction for " + e.field.name + " Price: " + e.field.price
            //graphic = new ImageView(this.getClass.getResource("login_icon.png").toString)
        }
        val lvPlayerBids = new ListView[String] {
            this.setId("lvPlayerBids")
            orientation = Orientation.Vertical
            items = ObservableBuffer()
        }
        //spieler raussuchen die bieten duerfen
        var bidders: Vector[PlayerInterface] = Vector[PlayerInterface]()
        var biddersWithdraw: Vector[Boolean] = Vector[Boolean]()
        for (player <- controller.players) {
            if (player.money > 0) {
                bidders = bidders :+ player
                biddersWithdraw = biddersWithdraw :+ false
            }
        }
        // spielerindex auf akuellen spieler setzen
        var playerIdx = bidders.indexOf(controller.players(controller.currentPlayer))
        // 1. gebot aufnehmen
        var auctionValue = 10 //Startvalue
        lvPlayerBids.getItems.add(0, bidders(playerIdx).name + " : " + auctionValue)
        val lblHighestBidder = new Text {
            text = "Highest bidding:" + bidders(playerIdx).name
            style = "-fx-font-size: 20pt"
            fill = new LinearGradient(
                endX = 0,
                stops = Stops(PaleGreen, SeaGreen))
        }
        // spielerindex auf 2. spieler setzen
        if (playerIdx + 1 == controller.players.length) playerIdx = 0
        else playerIdx += 1
        //dialog.getDialogPane.setPrefSize(600, 500)
        val image = new ImageView(new Image(e.field.image))
        val hbox1 = new HBox(lvPlayerBids, image)
        var playerStatsString = ""
        for (bidder <- bidders) {
            playerStatsString = playerStatsString + bidder.name + " money: " + bidder.money + "\n"
        }
        val lblPlayerStats = new Text {
            text = playerStatsString
            style = "-fx-font-size: 20pt"
            fill = new LinearGradient(
                endX = 0,
                stops = Stops(PaleGreen, SeaGreen))
        }

        val lblPlayerName = new Text {
            text = bidders(playerIdx).name + " is currently bidding"
            style = "-fx-font-size: 20pt"
            fill = new LinearGradient(
                endX = 0,
                stops = Stops(PaleGreen, SeaGreen))
        }
        val buttonTypeCancel = new ButtonType("Close", ButtonData.CancelClose)
        dialog.dialogPane().buttonTypes = Seq(buttonTypeCancel)
        // init buttons
        val btnCancel = dialog.dialogPane().lookupButton(buttonTypeCancel)
        btnCancel.setDisable(true)
        val bid1Button = new Button {
            text = "Bid +1"
        }
        val bid10Button = new Button {
            text = "Bid +10"
        }
        val bid100Button = new Button {
            text = "Bid +100"
        }
        bid1Button.onAction = handle {
            if (bidders(playerIdx).money < auctionValue + 1) {
                bid1Button.setDisable(true)
            } else if (bidders(playerIdx).money < auctionValue + 10) {
                bid10Button.setDisable(true)
            } else if (bidders(playerIdx).money < auctionValue + 100) {
                bid100Button.setDisable(true)
            } else {
                lblHighestBidder.setText("Highest bidder: " + bidders(playerIdx).name)
                auctionValue += 1
            }
            val string = bidders(playerIdx).name + " : " + auctionValue
            lvPlayerBids.getItems.add(0, string)
            //naechster spieler ist an der reihe wenn er noch nciht raus ist
            do {
                if (playerIdx + 1 == bidders.length) {
                    playerIdx = 0
                }
                else playerIdx += 1
            } while (biddersWithdraw(playerIdx))
            lblPlayerName.setText(bidders(playerIdx).name + " is currently bidding")
            lblPlayerStats.setText(playerStatsString)
        }
        bid10Button.onAction = handle {
            if (bidders(playerIdx).money < auctionValue + 1) {
                bid1Button.setDisable(true)
            } else if (bidders(playerIdx).money < auctionValue + 10) {
                bid10Button.setDisable(true)
            } else if (bidders(playerIdx).money < auctionValue + 100) {
                bid100Button.setDisable(true)
            } else {
                lblHighestBidder.setText("Highest bidder: " + bidders(playerIdx).name)
                auctionValue += 10
            }
            val string = bidders(playerIdx).name + " : " + auctionValue
            lvPlayerBids.getItems.add(0, string)
            //naechster spieler ist an der reihe wenn er noch nciht raus ist
            do {
                if (playerIdx + 1 == bidders.length) {
                    playerIdx = 0
                }
                else playerIdx += 1
            } while (biddersWithdraw(playerIdx))
            lblPlayerName.setText(bidders(playerIdx).name + " is currently bidding")
            lblPlayerStats.setText(playerStatsString)
        }
        bid100Button.onAction = handle {
            if (bidders(playerIdx).money < auctionValue + 1) {
                bid1Button.setDisable(true)
            } else if (bidders(playerIdx).money < auctionValue + 10) {
                bid10Button.setDisable(true)
            } else if (bidders(playerIdx).money < auctionValue + 100) {
                bid100Button.setDisable(true)
            } else {
                lblHighestBidder.setText("Highest bidder: " + bidders(playerIdx).name)
                auctionValue += 100
            }
            val string = bidders(playerIdx).name + " : " + auctionValue
            lvPlayerBids.getItems.add(0, string)
            //naechster spieler ist an der reihe wenn er noch nciht raus ist
            do {
                if (playerIdx + 1 == bidders.length) {
                    playerIdx = 0
                }
                else playerIdx += 1
            } while (biddersWithdraw(playerIdx))
            lblPlayerName.setText(bidders(playerIdx).name + " is currently bidding")
            lblPlayerStats.setText(playerStatsString)
        }

        val withdrawButton = new Button {
            text = "Withdraw"
            onAction = handle {
                // spieler rausnehmen
                biddersWithdraw = biddersWithdraw.updated(playerIdx, true)
                //naechster spieler ist an der reihe wenn er noch nciht raus ist
                do {
                    if (playerIdx + 1 == bidders.length) {
                        playerIdx = 0
                    }
                    else playerIdx += 1
                } while (biddersWithdraw(playerIdx))
                lblPlayerName.setText(bidders(playerIdx).name + " is currently bidding")
                //wenn alle raus sind ausser 1 spieler dann close
                if (biddersWithdraw.filter(_ == false).length == 1) {
                    btnCancel.setDisable(false)
                    bid1Button.setDisable(true)
                    bid10Button.setDisable(true)
                    bid100Button.setDisable(true)
                    this.setDisable(true)
                }
                //val cancelButton = dialog.getDialogPane().lookupButton( buttonTypeCancel ).asInstanceOf[Button]
                //cancelButton.fire
            }
        }
        val hbox = new HBox(
            bid1Button,
            bid10Button,
            bid100Button,
            withdrawButton,
        )

        val vbox = new VBox(lblPlayerStats, lblPlayerName, hbox, lblHighestBidder, hbox1)

        vbox.padding = Insets(20, 100, 10, 10)

        dialog.dialogPane().content = vbox
        dialog.resultConverter = dialogButton => {
            if (dialogButton == buttonTypeCancel) Result("Close")
            else null
        }
        // init bid buttons
        if (bidders(playerIdx).money < auctionValue + 1) {
            bid1Button.setDisable(true)
        } else if (bidders(playerIdx).money < auctionValue + 10) {
            bid10Button.setDisable(true)
        } else if (bidders(playerIdx).money < auctionValue + 100) {
            bid100Button.setDisable(true)
        }

        dialog.initStyle(StageStyle.Undecorated)
        val result = dialog.showAndWait()

        result match {
            case Some(Result("Close")) => {
                //todo closebutton
                //get highest bidder
                val nameValue = lvPlayerBids.getItems.get(0).toString.split(":").map(_.trim)
                // gebot abziehen
                bidders = bidders.updated(playerIdx, bidders(bidders.indexWhere(_.name == nameValue(0))).decMoney(nameValue(1).toInt))
                // besitzer setzen
                controller.board = controller.board.updated(controller.board.indexWhere(_.name == e.field.name),
                    e.field.setOwner(controller.players.indexWhere(_.name == bidders(playerIdx).name)))
                // spieler update
                for (player <- controller.players) {
                    for (bidder <- bidders) {
                        if (player.name == bidder.name) {
                            controller.players = controller.players.updated(controller.players.indexWhere(_.name == bidder.name), bidder)
                        }
                    }
                }
                controller.notifyObservers(AuctionEndedEventTui(bidders(bidders.indexWhere(_.name == nameValue(0))), e.field))
            }
        }
    }

    def rollForPosDialog(e: OpenRollForPosDialogEvent): Unit = {
        case class Result(option: String)

        val dialog = new Dialog[Result]() {
            headerText = "Player " + e.player.name + " roll for starting position"
        }

        val image = new ImageView(new Image(e.player.figure, 200, 200, true, true))

        val lblDiceResult = new Text {
            text = "Rolled: "
            style = "-fx-font-size: 20pt"
            fill = new LinearGradient(
                endX = 0,
                stops = Stops(PaleGreen, SeaGreen))
        }

        val buttonTypeCancel = new ButtonType("Close", ButtonData.CancelClose)
        dialog.dialogPane().buttonTypes = Seq(buttonTypeCancel)
        // init buttons
        val btnCancel = dialog.dialogPane().lookupButton(buttonTypeCancel)
        btnCancel.setDisable(true)
        val rollDiceButton = new Button {
            text = "Roll dice"
        }
        val diceThrow = controller.wuerfeln
        rollDiceButton.onAction = handle {
            lblDiceResult.setText("Rolled: " + (diceThrow._1 + diceThrow._2))
            btnCancel.setDisable(false)
            rollDiceButton.setDisable(true)
        }

        val vbox = new VBox(image, rollDiceButton, lblDiceResult)

        vbox.padding = Insets(50, 100, 20, 20)

        dialog.dialogPane().content = vbox
        dialog.resultConverter = dialogButton => {
            if (dialogButton == buttonTypeCancel) Result("Close")
            else null
        }

        dialog.initStyle(StageStyle.Undecorated)
        val result = dialog.showAndWait()

        result match {
            case Some(Result("Close")) => {
                controller.players = controller.players.updated(controller.currentPlayer,
                    controller.players(controller.currentPlayer).setRollForPosition(diceThrow._1 + diceThrow._2))
            }
        }
    }

    def informationDialog(e: OpenInformationDialogEvent): Unit = {
        new Alert(AlertType.Information) {
            title = "Monopoly SE"
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

        }
        for (player <- controller.players) {
            if (player.money > 0) {
                alert.contentText = "Game over, Winner is " + player.name
                alert.graphic = new ImageView(new Image(player.figure))
                controller.notifyObservers(gameFinishedEventTui(player))
            }
        }
        alert.initStyle(StageStyle.Undecorated)
        alert.showAndWait()
    }

    def playerFreeDialog(e: OpenPlayerFreeDialog): Unit = {
        // todo
        val alert = new Alert(AlertType.Confirmation) {
            headerText = e.player.name + "  is free again."
            graphic = new ImageView(new Image(e.player.figure))
        }
        alert.initStyle(StageStyle.Undecorated)
        val result = alert.showAndWait()

        result match {
            case Some(ButtonType.OK) => println("ok in jail dialog")
            case _ => "Cancel"
        }
    }

    def goToJailPaschDialog(e: openGoToJailPaschDialog): Unit = {
        // todo
        val alert = new Alert(AlertType.Confirmation) {
            headerText = controller.paschCount + " Pasch gewuerfelt"
            contentText = "Go to jail"
            graphic = new ImageView(new Image("file:images/inJail.png"))
        }
        alert.initStyle(StageStyle.Undecorated)
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

        val image = new ImageView(new Image(e.field.image))

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 0, 0)
        }
        dialog.dialogPane().content = grid
        dialog.initStyle(StageStyle.Undecorated)
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

        var drawnCard = ""
        try {
            drawnCard = controller.chanceCards.head
        }
        catch {
            case e: Exception => {
                controller.chanceCards = controller.cards.shuffleChanceCards(controller.chanceCardsList)
                drawnCard = controller.chanceCards.head
            }
        }
        val image = new ImageView(drawnCard)
        controller.chanceCards = controller.cards.drawCard(controller.chanceCards)

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 0, 0)
        }
        dialog.dialogPane().content = grid
        dialog.initStyle(StageStyle.Undecorated)
        dialog.showAndWait()

        drawnCard match {
            case d if d == controller.chanceCardsList.head => {
                // card 1 action here
            }
            case d if d == controller.chanceCardsList(1) => {
                // card 2 action here
            }
            case _ => // todo until action 13 .chanceCardsList(12)

        }
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

        var drawnCard = ""
        try {
            drawnCard = controller.communityChestCards.head
        }
        catch {
            case e: Exception => {
                controller.communityChestCards = controller.cards.shuffleCommunityChestCards(controller.communityChestCardsList)
                drawnCard = controller.communityChestCards.head
            }
        }
        val image = new ImageView(drawnCard)
        controller.communityChestCards = controller.cards.drawCard(controller.communityChestCards)


        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid
        dialog.initStyle(StageStyle.Undecorated)
        dialog.showAndWait()
        println("drawnCard" + drawnCard)
        drawnCard match {
            case "file:images/AdvanceToGoChance.jpg" => {

            }
            case "file:images/AdvanceToGoChest.jpg" => {

            }
            case _ => throw new UnsupportedOperationException
        }
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
        dialog.initStyle(StageStyle.Undecorated)
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
        val image = new ImageView(new Image(controller.players(controller.currentPlayer).figure, 150, 150, true, true))

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 0, 0)
        }
        dialog.dialogPane().content = grid
        dialog.initStyle(StageStyle.Undecorated)
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

        val image = new ImageView(new Image(controller.players(controller.currentPlayer).figure))

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 0, 0)
        }
        dialog.dialogPane().content = grid
        dialog.initStyle(StageStyle.Undecorated)
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

        val image = new ImageView(new Image(e.field.image))

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid
        dialog.initStyle(StageStyle.Undecorated)
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
        dialog.initStyle(StageStyle.Undecorated)
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

        val image = new ImageView(new Image(e.field.image))

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid
        dialog.initStyle(StageStyle.Undecorated)
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

        val image = new ImageView(new Image(e.field.image))

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid
        dialog.initStyle(StageStyle.Undecorated)
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

        val image = new ImageView(new Image(e.field.image))

        val grid = new GridPane() {
            hgap = 10
            vgap = 10
            padding = Insets(20, 100, 10, 10)
            add(image, 2, 0)
        }
        dialog.dialogPane().content = grid
        dialog.initStyle(StageStyle.Undecorated)
        dialog.showAndWait()
        controller.players = controller.players.updated(controller.currentPlayer, controller.players(controller.currentPlayer).decMoney(75))
        controller.collectedTax += 75
        controller.checkPlayerDept(-1)
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

        val image = new ImageView(new Image(e.field.image))

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
        dialog.initStyle(StageStyle.Undecorated)
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
        controller.checkPlayerDept(-1)
    }
}