package controller.controllerComponent.controllerBaseImpl

import Game.MonopolyModule
import com.google.inject.{Guice, Inject}
import controller.controllerComponent.ControllerInterface
import model._
import model.fileIOComponent.FileIOInterface
import model.playerComponent.Player
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.image.{Image, ImageView}
import util.UndoManager

class Controller @Inject()(val fileIo: FileIOInterface, val cards: CardsInterface, val dice: DiceInterface) extends ControllerInterface {
    val injector = Guice.createInjector(new MonopolyModule)
    val undoManager = new UndoManager
    var humanPlayers = 0
    var npcPlayers = 0
    var board: Vector[Cell] = Vector[Cell]()
    val chanceCardsList: List[String] = List[String](
        "file:images/Pay15Chance.png",
        "file:images/PayEachPlayer50Chance.png",
        "file:images/Collect50Chance.png",
        "file:images/Collect150Chance.png",
        "file:images/GoBack3SpacesChance.png",
        "file:images/GoToBroadwalkChance.png",
        "file:images/GoToIllinoisAveChance.png",
        "file:images/GoToJailChance.png",
        "file:images/GoToNextUtilityThrowDicePay10TimesChance.png",
        "file:images/GoToReadingChance.png",
        "file:images/GoToStCharlesPlaceChance.png",
        "file:images/JailFreeCardChance.png",
        "file:images/Pay25ForHouse100ForHotelChance.png")
    var chanceCards: Vector[String] = cards.shuffleChanceCards(chanceCardsList)
    val communityChestCardsList: List[String] = List[String](
        "file:images/Receive25Chest.png",
        "file:images/PayHostpital100Chest.png",
        "file:images/Pay50Chest.png",
        "file:images/Pay150Chest.png",
        "file:images/IncomeTaxChest.png",
        "file:images/Collect10Chest.png",
        "file:images/Collect45Chest.png",
        "file:images/Collect100Chest.png",
        "file:images/Inherit100Chest.png",
        "file:images/Collect100Chest2.png",
        "file:images/Collect200Chest.png",
        "file:images/Collect50FromEveryPlayerChest.png",
        "file:images/GoToJailChest.png",
        "file:images/JailFreeCardChest.png",
        "file:images/GoToNextRailroadPayTwiceChest.png",
        "file:images/Pay40ForHouse155ForHotelChest.png")
    var communityChestCards: Vector[String] = cards.shuffleCommunityChestCards(communityChestCardsList)

    var players: Vector[PlayerInterface] = Vector[PlayerInterface]()
    var playerNames: Vector[String] = Vector[String]()
    var remainingFiguresToPick: List[String] = List[String]("Fingerhut",
        "Hut",
        "Schubkarre",
        "Schuh",
        "Hund",
        "Auto",
        "Bügeleisen",
        "Schiff"
    )
    var playerFigures: Vector[String] = Vector[String]()
    var npcNames: Vector[String] = Vector[String]()
    var round = 1
    var currentStage = new PrimaryStage()
    var currentPlayer = 0 // aktueller spieler
    var paschCount = 0
    // feldcoords todo resizeable mainwindow offset xy stackpane
    val fieldCoordsX = List[Double](
        350, 280, 210, 140, 70, 0, -70, -140, -210, -280, -350,
        -350, -350, -350, -350, -350, -350, -350, -350, -350, -350,
        -280, -210, -140, -70, 0, 70, 140, 210, 280, 350,
        350, 350, 350, 350, 350, 350, 350, 350, 350)

    val fieldCoordsY = List[Double](
        350, 350, 350, 350, 350, 350, 350, 350, 350, 350, 350,
        280, 210, 140, 70, 0, -70, -140, -210, -280, -350,
        -350, -350, -350, -350, -350, -350, -350, -350, -350, -350,
        -280, -210, -140, -70, 0, 70, 140, 210, 280)
    var collectedTax = 0

    // todo save gamestate and load gamestate
    var tmpHumanPlayers = humanPlayers
    var tmpNpcPlayers = npcPlayers
    var tmpBoard = board
    var tmpPlayers = players
    var tmpRound = round
    var tmpCurrentPlayer = currentPlayer
    var tmpCollectedTax = collectedTax

    def createGame(playerNames: Vector[String], npcNames: Vector[String]): Unit = {
        players = createPlayers(playerNames, npcNames)
        board = createBoard
        humanPlayers = playerNames.length
        npcPlayers = npcNames.length
    }

    def checkGameOver(): Boolean = {
        var playerwithmoney = 0
        for (i <- players.indices) {
            if (players(i).money > 0) {
                playerwithmoney += 1
            }
        }
        playerwithmoney == 1
    }

    def payRent(field: Buyable) = {
        //miete abziehen
        players = players.updated(currentPlayer, players(currentPlayer).decMoney(field.rent))
        players = players.updated(field.owner, players(field.owner).incMoney(field.rent))
        notifyObservers(payRentEventTui(players(currentPlayer), players(field.owner)))
        // schauen ob player ins minus gekommen ist
        checkPlayerDept(field.owner)
    }

    def payTax = {
        ;
    }

    def payElse = {
        ;
    }

    def buyHome: Unit = {
        players = players.updated(currentPlayer, players(currentPlayer).decMoney(200))
        board = board.updated(players(currentPlayer).position, board(players(currentPlayer).position).asInstanceOf[Street].buyHome(1))
        notifyObservers(buyHouseEventTui(players(currentPlayer), board(players(currentPlayer).position).asInstanceOf[Street]))
    }

    def checkPlayerDept(ownerIdx: Int) = {
        if (players(currentPlayer).money > 0) {
            val endTurnButton = currentStage.scene().lookup("#endTurn").asInstanceOf[javafx.scene.control.Button]
            endTurnButton.setText("End turn")
        }
        else {
            notifyObservers(playerHasDeptEventTui(players(currentPlayer), ownerIdx))
            HumanOrNPCStrategy.execute("playerHasDept")
            //Disable roll button turn endturn button to declare bankrupt
            val rollDiceBUtton = currentStage.scene().lookup("#rollDice") //.asInstanceOf[javafx.scene.control.Button]
            rollDiceBUtton.setDisable(false)
            val endTurnButton = currentStage.scene().lookup("#endTurn").asInstanceOf[javafx.scene.control.Button]
            endTurnButton.setDisable(false)
            endTurnButton.setText("Declare Bankrupt")
        }

    }

    def clearGuiElements() = {
        val listviewPlayers = currentStage.scene().lookup("#lvPlayers").asInstanceOf[javafx.scene.control.ListView[String]]
        listviewPlayers.getItems.clear
        val listviewEventLog = currentStage.scene().lookup("#lvEventLog").asInstanceOf[javafx.scene.control.ListView[String]]
        listviewEventLog.getItems.clear
        // delete everything on the board
        val stackpane = currentStage.scene().lookup("#stackpane").asInstanceOf[javafx.scene.layout.StackPane]
        stackpane.getChildren().clear()
        val boardImage = new ImageView(new Image("file:images/BoardMonopolyDeluxe1992.png", 800, 800, false, true))
        stackpane.getChildren().add(boardImage)
    }


    def onQuit() = {
        notifyObservers(OpenConfirmationDialogEvent())
    }

    def onInformation() = {
        notifyObservers(OpenInformationDialogEvent())
    }

    def onSaveGame() = {
        notifyObservers(GameSavedEventTui())
        tmpHumanPlayers = humanPlayers
        tmpNpcPlayers = npcPlayers
        tmpBoard = board
        tmpPlayers = players
        tmpRound = round
        tmpCurrentPlayer = currentPlayer
        tmpCollectedTax = collectedTax
        fileIo.saveGame(this)
    }

    def onLoadGame() = {
        GameStates.handle(InitGameEvent())
        val updated = fileIo.loadGame
        humanPlayers = updated._1
        npcPlayers = updated._2
        board = updated._6
        players = updated._7
        round = updated._3
        paschCount = updated._4
        collectedTax = updated._5
        chanceCards = updated._8
        communityChestCards = updated._9
        for (i <- 0 until humanPlayers + npcPlayers) {
            currentPlayer = i
            val stackpane = currentStage.scene().lookup("#stackpane").asInstanceOf[javafx.scene.layout.StackPane]
            val figure = new ImageView(new Image(players(i).figure, 50, 50, true, true))
            figure.setId("#player" + i)
            stackpane.getChildren().add(figure)
            notifyObservers(MovePlayerFigureEvent(
                fieldCoordsX(players(currentPlayer).position),
                fieldCoordsY(players(currentPlayer).position)))
        }
        currentPlayer = updated._10
        notifyObservers(GameLoadedEventTui())
        notifyObservers(UpdateListViewPlayersEvent())
        GameStates.handle(StartFirstRoundEvent())


    }

    def onStartGame() = {
        GameStates.handle(InitGameEvent())
        notifyObservers(OpenGetPlayersDialogEvent())
    }

    def runNewGame() = {
        clearGuiElements()
        notifyObservers(NewGameStartedTui())
        notifyObservers(UpdateListViewPlayersEvent())
        GameStates.handle(getPlayersEvent())
        GameStates.handle(createBoardAndPlayersEvent())
        GameStates.handle(rollForPositionsEvent())
        GameStates.handle(StartFirstRoundEvent())
    }

    def onRollDice() = {
        HumanOrNPCStrategy.execute("rollDice") match {
            case (roll1: Int, roll2: Int, pasch: Boolean) => println(roll1, roll2, pasch)
                if (pasch) {
                    paschCount += 1
                    if (players(currentPlayer).jailCount > 0) {
                        players = players.updated(currentPlayer, players(currentPlayer).resetJailCount)
                        notifyObservers(playerIsFreeEventTui(players(currentPlayer)))
                        notifyObservers(OpenPlayerFreeDialog(players(currentPlayer)))
                    }
                } else {
                    // Disable rollbutton if no pasch and enable endturn button
                    val rollDiceBUtton = currentStage.scene().lookup("#rollDice") //.asInstanceOf[javafx.scene.control.Button]
                    rollDiceBUtton.setDisable(true)
                    val endTurnButton = currentStage.scene().lookup("#endTurn") //.asInstanceOf[javafx.scene.control.Button]
                    endTurnButton.setDisable(false)
                }
                //move to jail
                if (paschCount == 3) {
                    players = players.updated(currentPlayer, players(currentPlayer).moveToJail)
                    players = players.updated(currentPlayer, players(currentPlayer).incJailTime)
                    notifyObservers(MovePlayerFigureEvent(-350, 350)) // jailxy
                    notifyObservers(openGoToJailPaschDialog())
                    val rollDiceBUtton = currentStage.scene().lookup("#rollDice") //.asInstanceOf[javafx.scene.control.Button]
                    rollDiceBUtton.setDisable(true)
                    val endTurnButton = currentStage.scene().lookup("#endTurn") //.asInstanceOf[javafx.scene.control.Button]
                    endTurnButton.setDisable(false)
                } else {
                    // nur wenn player nicht im jail
                    if (players(currentPlayer).jailCount == 0) players = movePlayer(roll1 + roll2)
                }
        }
        notifyObservers(UpdateListViewPlayersEvent())

    }

    def onEndTurn() = {
        var gameOver = false
        val endTurnButton = currentStage.scene().lookup("#endTurn").asInstanceOf[javafx.scene.control.Button]
        if (endTurnButton.getText == "Declare Bankrupt") {
            notifyObservers(brokeEventTui(players(currentPlayer)))
            endTurnButton.setText("End turn")
            if (checkGameOver) {
                GameStates.handle(gameOverEvent())
                gameOver = true
            }
        }
        if (!gameOver) {
            // todo "its player x turn" notifyObservers(OpenNextPlayersTurnDialog())
            // init next round
            paschCount = 0
            // beim neuen zug spieler die kein geld mehr haben ueberspringen
            do {
                currentPlayer += 1
                if (currentPlayer == humanPlayers + npcPlayers) {
                    currentPlayer = 0 // erster spieler ist wieder dran
                    round += 1
                    //start next round
                    notifyObservers(newRoundEventTui(round))
                }
            } while (players(currentPlayer).money < 0)

            // update round label
            val lblPlayerTurn = currentStage.scene().lookup("#lblPlayerTurn").asInstanceOf[javafx.scene.text.Text]
            lblPlayerTurn.setText("It is " + players(currentPlayer).name + "´s turn")
            // Enable rollbutton and disable endturn button
            val rollDiceBUtton = currentStage.scene().lookup("#rollDice") //.asInstanceOf[javafx.scene.control.Button]
            rollDiceBUtton.setDisable(false)
            endTurnButton.setDisable(true)
            // todo hier iwo if botplayer run bot round -> roll ->  move -> end turn
            //start next turn
            //jailtime++
            if (players(currentPlayer).jailCount > 0) players = players.updated(currentPlayer, players(currentPlayer).incJailTime)
            // frei nach 3 runden
            if (players(currentPlayer).jailCount >= 4) {
                // player is free again notifyObservers(OpenInJailDialogEvent())
                players = players.updated(currentPlayer, players(currentPlayer).resetJailCount)
                notifyObservers(playerIsFreeEventTui(players(currentPlayer)))
                notifyObservers(OpenPlayerFreeDialog(players(currentPlayer)))
            }
            // wenn spieler im jail jaildialog oeffnen
            if (players(currentPlayer).jailCount > 0) {
                notifyObservers(playerInJailEventTui(players(currentPlayer)))
                notifyObservers(OpenInJailDialogEvent())
            }
            else {
                notifyObservers(normalTurnEventTui(players(currentPlayer)))
                HumanOrNPCStrategy.execute("normalTurn")
            }
            notifyObservers(UpdateListViewPlayersEvent())
            rollDiceBUtton.requestFocus() // zum durchentern
        }
    }

    object GameStates {

        var runState = initGameState

        def handle(e: GameStateEvent): Any = {
            e match {
                case e: InitGameEvent => runState = initGameState
                case e: getPlayersEvent => runState = getPlayersState(e)
                case e: rollForPositionsEvent => runState = rollForPositionsState
                case e: createBoardAndPlayersEvent => runState = createBoardAndPlayersState
                case e: StartFirstRoundEvent => runState = startFirstRound
                case e: gameOverEvent => runState = gameOverState

            }
            runState
        }

        def startFirstRound() = {
            val lblPlayerTurn = currentStage.scene().lookup("#lblPlayerTurn").asInstanceOf[javafx.scene.text.Text]
            lblPlayerTurn.setText("It is " + players(currentPlayer).name + "´s turn")
            val lblDiceResult = currentStage.scene().lookup("#lblDiceResult").asInstanceOf[javafx.scene.text.Text]
            lblDiceResult.setText("Result dice roll: ")
            val rollDiceBUtton = currentStage.scene().lookup("#rollDice") //.asInstanceOf[javafx.scene.control.Button]
            rollDiceBUtton.setDisable(false)
            //currentStage.scene().lookup("#endTurn")setDisable(false)
            // First round of the game starts here
            notifyObservers(newRoundEventTui(round))
            notifyObservers(normalTurnEventTui(players(currentPlayer)))
            HumanOrNPCStrategy.execute("normalTurn")
            // Enable roll and disable endturn button
            val rollDiceButton = currentStage.scene().lookup("#rollDice") //.asInstanceOf[javafx.scene.control.Button]
            rollDiceButton.setDisable(false)
            val endTurnButton = currentStage.scene().lookup("#endTurn") //.asInstanceOf[javafx.scene.control.Button]
            endTurnButton.setDisable(true)
            // enable save and load buttons
            //       todo use .getChildren().filtered(_.getId == "#player" + controller.currentPlayer)
            //       todo val menubar = currentStage.scene().lookup("#menubar").asInstanceOf[javafx.scene.control.MenuBar]
            //
            //        //println(menubar.getMenus().filtered(_ => ))
            //        val miSaveGame = currentStage.scene().lookup("#miSaveGame").asInstanceOf[MenuItem]
            //        miSaveGame.setDisable(false)
            //        val miLoadGame = currentStage.scene().lookup("#miLoadGame").asInstanceOf[MenuItem]
            //        miLoadGame.setDisable(false)

            //        do {

            //            GameStates.handle(checkGameOverEvent())
            //        } while (!gameOver)
            //        GameStates.handle(gameOverEvent())
            //GameStates.handle(InitGameEvent())
        }

        def getPlayersState(e: getPlayersEvent) = {
            // spieler mit namen einlesensr
            currentPlayer = 0
            while (currentPlayer < humanPlayers) {
                println("Enter name player " + (currentPlayer + 1) + ":")
                notifyObservers(OpenGetNameDialogEvent(currentPlayer)) // adds player in tui/gui... dialog
                currentPlayer += 1
            }
            for (i <- 0 until npcPlayers) {
                npcNames = npcNames :+ "NPC " + (i + 1)
            }
        }

        def rollForPositionsState = {
            notifyObservers(displayRollForPositionsEventTui())
            for (i <- 0 until humanPlayers + npcPlayers) {
                // jeden einmal wuerfeln lassen
                currentPlayer = i
                HumanOrNPCStrategy.execute("rollForPosition")
            }
            //nach reihenfolge sortieren
            players = players.sortBy(-_.rollForPosition) // - für reversed
            //reihenfolge erstmalig festlegen
            println("spieler mit reihenfolge:")
            for (i <- 0 until humanPlayers + npcPlayers) {
                players = players.updated(i, players(i).setTurnPosition(i))
                println(players(i))
            }
            // Spieler suchen die das gleiche gewuerfelt haben
            var rolledSame: Vector[PlayerInterface] = Vector[PlayerInterface]()
            for (i <- 0 until humanPlayers + npcPlayers) {
                for (j <- (i + 1) until humanPlayers + npcPlayers) {
                    if (players(i).rollForPosition == players(j).rollForPosition) {
                        // nur die die noch nicht drinnen sind hinzufuegen
                        if (!rolledSame.exists(_.name == players(i).name)) {
                            rolledSame = rolledSame :+ players(i)
                        }
                        if (!rolledSame.exists(_.name == players(j).name)) {
                            rolledSame = rolledSame :+ players(j)
                        }

                    }
                }
            }
            println("leute die nochmal würfeln dürfen ")
            for (player <- rolledSame) println(player)
            //todo lass alle die jeweils das gleiche gewuerfelt haben
            // so lange nochmal würfeln aber nur ihre position untereinander tauschen
            // bis keiner mehr das gleiche würfelt
            //
            //            var playersRollingAgain: Vector[Player] = Vector[Player]()
            //            var playersRollingAgainPositions: Vector[Int] = Vector[Int]()
            //            //while (!(rolledSame.isempty)) { // solange es spieler gibt die das gleiche gewuerfelt haben
            //            for (i <- 2 until 12) {
            //                //jede augenzahl durchgehen
            //                for (player <- players) {
            //                    // spieler die diese augenzahl gewürfelt haben raussuchen
            //                    // auch die positionen raussuchen
            //                    // z.b. a und b haben beide 9 und wuerfeln nochmal für pos 1 und 2
            //                    if (player.rollForPosition == i) {
            //                        playersRollingAgain :+ player
            //                        playersRollingAgainPositions :+ player.turnPosition
            //                    }
            //                }
            //                // spieler nochmal so lange wuerfeln lassen bis sie nichtmehr das gleiche haben
            //                // do
            //                for (player <- playersRollingAgain) {
            //                    //todo if player is npc or not
            //                    val rollDices = playerController.wuerfeln
            //                    println(player.name + " rolled " + (rollDices._1 + rollDices._2))
            //                    //ergebnis speichern für jeden spieler
            //                    players = players.updated(i, players(i).setRollForPosition(rollDices._1 + rollDices._2))
            //                }
            //                // while not das gleiche gewuerfelt
            //                // position untereinander bestimmten
            //                //sort players nach augenzahl absteigend
            //                // positionen sollten aufsteigend sein
            //                //var i = 0 ( i ist index von spielern sortiert, der mit höchsten ist 1.)
            //                // for (position <- positions)
            //                // players(i).setTurnPosition(position) ( und bekommt die 1. position )
            //                //i += 1
            //            }
            //
            //            //} END while
            //
            //
            //            // entgueltige reihenfolge festlegen
            notifyObservers(UpdateListViewPlayersEvent())
        }

        def createBoardAndPlayersState = {
            players = createPlayers(playerNames, npcNames)
            board = createBoard
            for (i <- 0 until humanPlayers + npcPlayers) {
                currentPlayer = i
                val stackpane = currentStage.scene().lookup("#stackpane").asInstanceOf[javafx.scene.layout.StackPane]
                val figure = new ImageView(new Image(players(i).figure, 50, 50, true, true))
                figure.setId("#player" + i)
                stackpane.getChildren().add(figure)
                notifyObservers(MovePlayerFigureEvent(350, 350))
            }
            //todo notifyObservers(e: GuiPutPlayersOnTheBoardEvent)
            // todo game starts here
            val lblPlayerTurn = currentStage.scene().lookup("#lblPlayerTurn").asInstanceOf[javafx.scene.text.Text]
            lblPlayerTurn.setText("Roll for positions")
            val lblDiceResult = currentStage.scene().lookup("#lblDiceResult").asInstanceOf[javafx.scene.text.Text]
            lblDiceResult.setText("Result dice roll: ")
        }

        def gameOverState = {
            val rollDiceButton = currentStage.scene().lookup("#rollDice")
            rollDiceButton.setDisable(true) //.asInstanceOf[javafx.scene.control.Button]
            val endTurnButton = currentStage.scene().lookup("#endTurn") //.asInstanceOf[javafx.scene.control.Button]
            endTurnButton.setDisable(true)
            val lblPlayerTurn = currentStage.scene().lookup("#lblPlayerTurn").asInstanceOf[javafx.scene.text.Text]
            lblPlayerTurn.setText("Game Over")
            notifyObservers(openGameOverDialogEvent())

        }

        def initGameState: Unit = {
            clearGuiElements()

            humanPlayers = 0
            npcPlayers = 0
            board = Vector[Cell]()
            players = Vector[Player]()
            playerNames = Vector[String]()
            remainingFiguresToPick = List[String]("Fingerhut",
                "Hut",
                "Schubkarre",
                "Schuh",
                "Hund",
                "Auto",
                "Bügeleisen",
                "Schiff"
            )
            playerFigures = Vector[String]()
            npcNames = Vector[String]()
            round = 1
        }

    }

    object HumanOrNPCStrategy {
        def execute(option: String): Any = {
            val isNpc = players(currentPlayer).isNpc
            option match {
                case "normalTurn" =>
                    normalTurn(isNpc)
                case "buyableStreet" =>
                    buyableStreet(isNpc)
                case "payRent" =>
                    pay(isNpc)
                case "rollForPosition" =>
                    rollForPositions(isNpc)
                case "playerHasDept" =>
                    dept(isNpc)
                case "rollDice" =>
                    roll(isNpc)
                case _ =>
            }
        }

        def normalTurn(isNpc: Boolean) = {
            if (isNpc) onRollDice()
            else notifyObservers(OpenNormalTurnDialogEvent(players(currentPlayer)))
        }

        def buyableStreet(isNpc: Boolean) = {
            val field = board(players(currentPlayer).position).asInstanceOf[Buyable]
            if (isNpc) {
                if (players(currentPlayer).money >= field.price) buy(field)
                // todo else auction
                onEndTurn()
            }
            else notifyObservers(OpenBuyableFieldDialog(field))
        }

        def pay(isNpc: Boolean) = {
            val field = board(players(currentPlayer).position).asInstanceOf[Buyable]
            if (isNpc) {
                payRent(field)
                checkPlayerDept(field.owner)
                onEndTurn()
            } else notifyObservers(OpenPayRentDialog(
                board(players(currentPlayer).position).asInstanceOf[Buyable]))
        }

        def rollForPositions(isNpc: Boolean) = {
            if (isNpc) wuerfeln
            else notifyObservers(OpenRollForPosDialogEvent(players(currentPlayer)))
        }

        def dept(isNpc: Boolean) = {
            val field = board(players(currentPlayer).position).asInstanceOf[Buyable]
            if (isNpc) onEndTurn() // npc geht direkt bankrott Todo npc verkaufen lassen bis im plus
            else notifyObservers(OpenPlayerDeptDialog(field.owner))
        }

        def roll(isNpc: Boolean) = {
            wuerfeln
        }

    }

    // todo for boardcontroller
    def newOwner(playerNr: Int, cell: Cell): Cell = {
        val updated = cell.asInstanceOf[Buyable].setOwner(playerNr)
        updated
    }

    def createBoard: Vector[Cell] = {
        // group 0 go, etc, group 1 water,electricity, group 2 railroads,
        var board: Vector[Cell] = Vector()
        board = board :+ CellFactory("Go", "Go", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/Go.png")
        board = board :+ CellFactory("Street", "Mediterranean Avenue", 3, 60, -1, 200, 0, mortgage = false, image = "file:images/MediterraneanAve.png")
        board = board :+ CellFactory("CommunityChest", "CommunityChest1", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/Go.png") // todo
        board = board :+ CellFactory("Street", "Baltic Avenue", 3, 60, -1, 200, 0, mortgage = false, image = "file:images/BalticAve.png")
        board = board :+ CellFactory("IncomeTax", "IncomeTax", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/IncomeTax.png")
        board = board :+ CellFactory("Street", "Reading Railroad", 2, 200, -1, 200, 0, mortgage = false, image = "file:images/ReadingRailroad.png")
        board = board :+ CellFactory("Street", "Oriental Avenue", 4, 100, -1, 200, 0, mortgage = false, image = "file:images/OrientalAve.png")
        board = board :+ CellFactory("Eventcell", "Eventcell1", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/OrientalAve") // todo
        board = board :+ CellFactory("Street", "Vermont Avenue", 4, 100, -1, 200, 0, mortgage = false, image = "file:images/VermontAve.png")
        board = board :+ CellFactory("Street", "Conneticut Avenue", 4, 120, -1, 200, 0, mortgage = false, image = "file:images/ConneticutAve.png")
        board = board :+ CellFactory("Jail", "Visit Jail", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/VisitJail.png")

        board = board :+ CellFactory("Street", "St. Charles Place", 5, 140, -1, 200, 0, mortgage = false, image = "file:images/StCharlesPlace.png")
        board = board :+ CellFactory("Street", "Electric Company", 1, 150, -1, 200, 0, mortgage = false, image = "file:images/ElectricCompany.png")
        board = board :+ CellFactory("Street", "States Avenue", 5, 140, -1, 200, 0, mortgage = false, image = "file:images/StatesAve.png")
        board = board :+ CellFactory("Street", "Virgina Avenue", 5, 160, -1, 200, 0, mortgage = false, image = "file:images/VirginiaAve.png")
        board = board :+ CellFactory("Street", "Pennsylvania Railroad", 2, 200, -1, 200, 0, mortgage = false, image = "file:images/PennsylvaniaRR.png")
        board = board :+ CellFactory("Street", "St. James Place", 6, 180, -1, 200, 0, mortgage = false, image = "file:images/StJamesPlace.png")
        board = board :+ CellFactory("CommunityChest", "CommunityChest2", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/OrientalAve") // todo
        board = board :+ CellFactory("Street", "Tennessee Avenue", 6, 180, -1, 200, 0, mortgage = false, image = "file:images/TennesseeAve.png")
        board = board :+ CellFactory("Street", "New York Avenue", 6, 200, -1, 200, 0, mortgage = false, image = "file:images/NewYorkAve.png")
        board = board :+ CellFactory("FreeParking", "Free parking", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/ParkFree.png")

        board = board :+ CellFactory("Street", "Kentucky Avenue", 7, 220, -1, 200, 0, mortgage = false, image = "file:images/KentuckyAve.png")
        board = board :+ CellFactory("Eventcell", "Eventcell2", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/OrientalAve") // todo
        board = board :+ CellFactory("Street", "Indiana Avenue", 7, 220, -1, 200, 0, mortgage = false, image = "file:images/IndianaAve.png")
        board = board :+ CellFactory("Street", "Illinois Avenue", 7, 240, -1, 200, 0, mortgage = false, image = "file:images/IllinoisAve.png")
        board = board :+ CellFactory("Street", "B & O Railroad", 2, 200, -1, 200, 0, mortgage = false, image = "file:images/BnORailroad.png")
        board = board :+ CellFactory("Street", "Atlantic Avenue", 8, 260, -1, 500, 0, mortgage = false, image = "file:images/AtlanticAve.png")
        board = board :+ CellFactory("Street", "Ventnor Avenue", 8, 260, -1, 800, 0, mortgage = false, image = "file:images/VentnorAve.png")
        board = board :+ CellFactory("Street", "Water Works", 1, 150, -1, 200, 0, mortgage = false, image = "file:images/WaterWorks.png")
        board = board :+ CellFactory("Street", "Marvin Gardens", 8, 280, -1, 2500, 0, mortgage = false, image = "file:images/MarvinGardens.png")
        board = board :+ CellFactory("GoToJail", "Go to jail", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/Jail.png")

        board = board :+ CellFactory("Street", "Pacific Avenue", 9, 300, -1, 200, 0, mortgage = false, image = "file:images/PacificAve.png")
        board = board :+ CellFactory("Street", "North Carolina Avenue", 9, 300, -1, 200, 0, mortgage = false, image = "file:images/NoCarolinaAve.png")
        board = board :+ CellFactory("CommunityChest", "CommunityChest3", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/OrientalAve") // todo
        board = board :+ CellFactory("Street", "Pennsylvania Avenue", 9, 320, -1, 200, 0, mortgage = false, image = "file:images/PennsylvaniaAve.png")
        board = board :+ CellFactory("Street", "Short Line Railroad", 2, 200, -1, 200, 0, mortgage = false, image = "file:images/ShortLineRR.png")
        board = board :+ CellFactory("Eventcell", "Eventcell3", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/OrientalAve") // todo
        board = board :+ CellFactory("Street", "Park place", 10, 350, -1, 200, 0, mortgage = false, image = "file:images/ParkPlace.png")
        board = board :+ CellFactory("AdditionalTax", "Luxuary Tax", 0, 0, 0, 0, 0, mortgage = false, image = "file:images/LuxuaryTax.png")
        board = board :+ CellFactory("Street", "Broadwalk", 10, 400, -1, 200, 0, mortgage = false, image = "file:images/Broadwalk.png")
        board
        // todo create xml or json from board
        // then add xml/json parser
    }

    def activateStart(field: Los): Unit = {
        field.onPlayerEntered(currentPlayer)
        notifyObservers(OpenPlayerEnteredGoDialog(field))
    }

    def activateStreet(field: Buyable): Unit = {
        if (field.owner == -1 && field.owner != currentPlayer) HumanOrNPCStrategy.execute("buyableStreet")
        else HumanOrNPCStrategy.execute("payRent")

    }

    def activateIncomeTax(field: IncomeTax): Unit = {
        field.onPlayerEntered(currentPlayer)
        notifyObservers(OpenIncomeTaxDialog(field))
    }

    def activateVisitJail(field: Jail): Unit = {
        field.onPlayerEntered(currentPlayer)
        notifyObservers(OpenVisitJailDialog(field))
    }

    def activateFreiParken(field: FreiParken): Unit = {
        field.onPlayerEntered(currentPlayer)
        notifyObservers(OpenParkFreeDialog(field))

    }

    def activateLuxuaryTax(field: Zusatzsteuer): Unit = {
        field.onPlayerEntered(currentPlayer)
        notifyObservers(OpenLuxuaryTaxDialog(field))

    }

    def activateChance(field: Eventcell): Unit = {
        field.onPlayerEntered(currentPlayer)
        notifyObservers(OpenChanceDialog())
    }

    def activateCommunityChest(field: CommunityChest): Unit = {
        field.onPlayerEntered(currentPlayer)
        notifyObservers(OpenCommunityChestDialog())
    }

    def activateJail(field: GoToJail): Unit = {
        field.onPlayerEntered(currentPlayer)
        notifyObservers(openGoToJailDialog(field))
        players = players.updated(currentPlayer, players(currentPlayer).moveToJail)
        players = players.updated(currentPlayer, players(currentPlayer).incJailTime)
        notifyObservers(MovePlayerFigureEvent(-350, 350)) // jailxy
        val rollDiceBUtton = currentStage.scene().lookup("#rollDice") //.asInstanceOf[javafx.scene.control.Button]
        rollDiceBUtton.setDisable(true)
        val endTurnButton = currentStage.scene().lookup("#endTurn") //.asInstanceOf[javafx.scene.control.Button]
        endTurnButton.setDisable(false)
    }


    object CellFactory {
        def apply(kind: String, name: String, group: Int, price: Int, owner: Int, rent: Int, home: Int, mortgage: Boolean,
                  image: String): Cell = kind match {
            case "Go" => Los(name, group, image: String)
            case "Street" => Street(name, group, price, owner, rent, home, mortgage, image: String)
            case "CommunityChest" => CommunityChest(name, group, image: String)
            case "IncomeTax" => IncomeTax(name, group, image: String)
            case "Eventcell" => Eventcell(name, group, image: String)
            case "Jail" => Jail(name, group, image: String)
            case "FreeParking" => FreiParken(name, group, image: String)
            case "GoToJail" => GoToJail(name, group, image: String)
            case "AdditionalTax" => Zusatzsteuer(name, group, image: String)
            case _ => throw new UnsupportedOperationException
        }
    }

    // todo playercontroller
    def createPlayers(playerNames: Vector[String], npcNames: Vector[String]) = {
        var players: Vector[PlayerInterface] = Vector()
        var i = 0
        for (name <- playerNames) {
            var player = injector.getInstance(classOf[PlayerInterface])
            player = player.setName(name)
            player = player.setFigure(playerFigures(i))
            players = players :+ player
            i += 1
        }
        i = 0
        for (name <- npcNames) {
            ///////////////
            // 1. verfügbare figur nehmen
            // todo try figure if is empty take picture for more npc than playerfigures nein
            //            var f1 = ""
            //            try {
            //                f1 = Some(gameController.remainingFiguresToPick.head).toString()
            //            }
            //            catch {
            //                case e: Exception => f1 = "Hut"
            //            }
            //            print("fi" + f1)
            //////////////////////
            val figure = remainingFiguresToPick.head
            val imgPath = figure match {
                case "Hut" => "file:images/Hut.jpg"
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
            remainingFiguresToPick = remainingFiguresToPick.filterNot(elm => elm == figure)
            players = players :+ playerComponent.Player(name, figure = imgPath, isNpc = true)
        }
        players
    }

    def buy(field: Buyable) = {
        players = players.updated(currentPlayer, players(currentPlayer).decMoney(field.price))
        // spieler.besitz add streetnr
        board = board.updated(players(currentPlayer).position, field.setOwner(currentPlayer))
        notifyObservers(buyStreetEventTui(players(currentPlayer), field))
    }

    def wuerfeln: (Int, Int, Boolean) = {
        val roll1 = dice.roll
        val roll2 = dice.roll
        var pasch = false
        if (dice.checkPash(roll1, roll2)) {
            pasch = true
        }
        notifyObservers(diceEventTui(players(currentPlayer), roll1, roll2, pasch))
        notifyObservers(UpdateGuiDiceLabelEvent(roll1, roll2, pasch))
        (roll1, roll2, pasch)
    }

    def movePlayer(sumDiceThrow: Int): Vector[PlayerInterface] = {
        // spieler bewegen
        players = players.updated(currentPlayer, players(currentPlayer).move(sumDiceThrow))
        println("sumDiceThrow = " + sumDiceThrow)
        // schauen ob über los gegangen todo wenn spieler auf jail kommt und pasch gewuerfelt hat
        if (players(currentPlayer).position >= 40) {
            notifyObservers(playerWentOverGoEventTui(players(currentPlayer)))
            if (!(players(currentPlayer).position == 0)) // falls spieler nicht auf los sonst 2 dialoge
                notifyObservers(OpenPlayerPassedGoDialog())
            players = players.updated(currentPlayer, players(currentPlayer).moveBack(40))
        }
        ////////////MoveplayerAfterRollDice////////////////
        notifyObservers(MovePlayerFigureEvent(
            fieldCoordsX(players(currentPlayer).position),
            fieldCoordsY(players(currentPlayer).position)))

        // neue position ausgeben
        notifyObservers(playerMoveEventTui(players(currentPlayer), board(players(currentPlayer).position)))
        // aktion fuer betretetenes feld ausloesen
        val field = board(players(currentPlayer).position)
        field match {
            case e: Buyable => activateStreet(e.asInstanceOf[Buyable])
            case e: Los => activateStart(e.asInstanceOf[Los])
            case e: Eventcell => activateChance(e.asInstanceOf[Eventcell])
            case e: CommunityChest => activateCommunityChest(e.asInstanceOf[CommunityChest])
            case e: Jail => activateVisitJail(e.asInstanceOf[Jail])
            case e: IncomeTax => activateIncomeTax(e.asInstanceOf[IncomeTax])
            case e: FreiParken => activateFreiParken(e.asInstanceOf[FreiParken])
            case e: GoToJail => activateJail(e.asInstanceOf[GoToJail])
            case e: Zusatzsteuer => activateLuxuaryTax(e.asInstanceOf[Zusatzsteuer])
            case _ => throw new UnsupportedOperationException
        }
        players
    }
}