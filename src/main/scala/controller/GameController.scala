package controller

import java.util.{Timer, TimerTask}

import model._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.control.Label
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.StackPane
import util.{Observable, UndoManager}

class GameController extends Observable {
    val dice = Dice()
    val undoManager = new UndoManager
    val playerController = new PlayerController(this)
    val boardController = new BoardController(this)
    var humanPlayers = 0
    var npcPlayers = 0
    var playerCount = 0
    var board: Vector[Cell] = Vector[Cell]()
    var players: Vector[Player] = Vector[Player]()
    var playerNames: Vector[String] = Vector[String]()
    var remainingFiguresToPick = List[String]("Fingerhut",
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
    var answer = ""
    var gameOver = false // todo wie über states
    //todo gui
    var currentStage = new PrimaryStage()
    var isturn = 0 // aktueller spieler
    // feldcoords todo in liste mit tupel
    val goXY = (350, 350)
    val jailXY = (-350, 350)
    val ParkFreeXY = (-350, -350)
    val GoToJailXy = (350, -350)
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


    def createGame(playerNames: Vector[String], npcNames: Vector[String]): Unit = {
        players = playerController.createPlayers(playerNames, npcNames)
        board = boardController.createBoard
        humanPlayers = playerNames.length
        npcPlayers = npcNames.length
        notifyObservers(askUndoGetPlayersEvent())
        if (answer == "yes") {
            undoManager.undoStep
        }
    }

    def checkGameOver(): (Boolean) = {
        var playerwithmoney = 0
        var winner = 0
        for (i <- players.indices) {
            if (players(i).money > 0) {
                playerwithmoney += 1
                winner = i
            }
        }
        //(playerwithmoney == 1, winner)
        playerwithmoney == 1
    }

    def runRound: Unit = {
        for (i <- players.indices) {
            isturn = i
            if (players(isturn).money > 0) {
                val roll = rollDice
                if (roll._2)
                    players = players.updated(i, players(i).moveToJail)
                else {
                    players = playerController.movePlayer(roll._1)
                    val option = board(players(i).position).onPlayerEntered(i)
                    players(isturn).strategy.execute(option)
                }
            }
        }
        round += 1
    }

    def rollDice: (Int, Boolean) = {
        var jailtime = false
        var paschcount = 0
        var sum = 0
        var rolls = 1
        while (rolls != 0) {
            val roll1 = dice.roll
            val roll2 = dice.roll
            sum += roll1 + roll2
            if (dice.checkPash(roll1, roll2)) {
                paschcount += 1
                rolls += 1
            }
            rolls -= 1
        }
        if (paschcount >= 3)
            jailtime = true
        (sum, jailtime)
    }

    def payRent: Unit = {
        val updated = playerController.payRent(board(players(isturn).position).asInstanceOf[Buyable])
        players = updated._2
        board = updated._1
    }

    def buy: Unit = {
        val updated = playerController.buy(board(players(isturn).position).asInstanceOf[Buyable])
        board = updated._1
        players = updated._2
    }

    def buyHome: Unit = {
        val updated = boardController.buyHome(board(players(isturn).position).asInstanceOf[Street])
        board = updated._1
        players = updated._2
    }

    def printFun(e: PrintEvent): Unit = {
        notifyObservers(e)
    }


    ///////////todo GUI  ///////////////////////////////////////////

    // functions

    // todo currentstage kann raus tui hat ehe controller

    def onQuit() = {
        notifyObservers(OpenConfirmationDialogEvent(currentStage))
    }

    def onInformation() = {
        notifyObservers(OpenInformationDialogEvent(currentStage))
    }

    def onStartGame() = {
        val playerMoveTest = false
        if (playerMoveTest) {
            notifyObservers(OpenGameWindowEvent())
            movePlayerTimerGui

        } else {
            notifyObservers(OpenGameWindowEvent())
            notifyObservers(OpenGetPlayersDialogEvent(currentStage))
            GameStates.handle(getPlayersEvent(humanPlayers, npcPlayers))
            GameStates.handle(createBoardAndPlayersEvent(playerNames, npcNames))
            notifyObservers(printEverythingEvent())
            notifyObservers(displayRollForPositionsEvent())
            GameStates.handle(rollForPositionsEvent())
            do {
                GameStates.handle(runRoundEvent())
                GameStates.handle(checkGameOverEvent())
            } while (!gameOver)
            GameStates.handle(gameOverEvent())
            //GameStates.handle(InitGameEvent())
            notifyObservers(OpenMainWindowEvent())

            // todo try} while(!GameStates.runState == GameStates.gameOverState))
        }


    }

    def movePlayerTimerGui(): Unit = {
        val len = fieldCoordsX.length
        var i = 0
        val timer: Timer = new Timer()
        timer.schedule(new TimerTask {
            override def run(): Unit = {
                println(i + 1)
                //movePlayerGui(fieldCoordsX(i), fieldCoordsY(i))
                i += 1
                if (i == len) timer.cancel()
                timer.purge()
            }
        }, 1000, 500)


    }

    def movePlayerGui(playerFigure: ImageView, x: Double, y: Double): Unit = {
        //val playerImage = currentStage.scene().lookup("#playerimage")
        println("moveplayer x y " + x + y)
        //playerImage.setTranslateX(x)
        //playerImage.setTranslateY(y)
        playerFigure.setTranslateX(x)
        playerFigure.setTranslateY(y)

    }

    def tryDrawOnGui(): Unit = {
        val playerLabel = new Label("onstack\nPlayer\nLabel")
        val stackpane = currentStage.scene().lookup("#stackpane").asInstanceOf[StackPane]
        stackpane.children
        //print(childs)
        //        val testpane = currentStage.scene().lookup("#stackpane") match {
        //            case e: StackPane => print(e.getChildren())
        //            case e: Image => print("image")
        //            case _ => print("null")
        //        }
        //print(testpane.getChildren())
        //.add(new Text("onstack\nPlayer\nLabel"))
        //testpane.getChildren().add(playerLabel)
        // todo spieler erstellen nachdem sie namen und bild gewaehlt haben
        //  HBox parent = new HBox();
        //  for (int i = 0; i < N_COLS, i++) {
        //    Node childNode = createNode();
        //    childNode.setId("child" + i);
        //    parent.getChildren().add(childNode);
        //} add players on board in loop
    }

    object PlayerTurnStrategy extends Observable {

        //todo var executePlayerTurn: Unit = { ???????????????????????
        def executePlayerTurn = {
            if (players(isturn).jailCount > -1) turnInJail else normalTurn

        }

        def normalTurn = {
            notifyObservers(normalTurnEvent(players(isturn)))

            players(isturn).strategy.execute("normalTurn") // todo zug für spieler oder npc

            //maybe  PlayerIsHumanOrNpcStrategy.selectOption(playerController.checkHypothek)

            //playerController.checkHypothek() // schauen ob haeuser im besitz hypotheken haben und bezahlen wenns geht

            // init pasch
            var pasch = true
            var paschCount = 0
            // wuerfeln
            while (pasch) {
                players(isturn).strategy.execute("rollDice") match {
                    case (roll1: Int, roll2: Int, rolledPasch: Boolean) => {
                        notifyObservers(diceEvent(roll1, roll2, rolledPasch))
                        if (rolledPasch) paschCount += 1
                        else pasch = false
                        //3x pasch gleich jail sonst move player
                        if (paschCount == 3) {
                            players = players.updated(isturn, players(isturn).moveToJail)
                            notifyObservers(playerMoveToJail(players(isturn)))
                        } else players = playerController.movePlayer(roll1 + roll2)
                    }
                }
            }
        }

        def turnInJail = {
            notifyObservers(playerInJailEvent(players(isturn)))
            players(isturn).strategy.execute("turnInJail") match {
                case (option: String) => {
                    // ...frei kaufen...
                    if (option == "buyOut") {
                        players = players.updated(isturn, players(isturn).decMoney(200))
                        //playerController.checkDept(-1) // owner = bank
                        players = players.updated(isturn, players(isturn).resetJailCount)
                        notifyObservers(playerIsFreeEvent(players(isturn)))
                        normalTurn // frei -> normalen zug ausfuehren
                    }
                    // ...die freikarte benutzen....
                    if (option == "useCard") {
                        notifyObservers(playerIsFreeEvent(players(isturn)))
                        normalTurn // frei -> normalen zug ausfuehren
                    }
                    // ...oder pasch wuerfeln.
                    if (option == "rollDice") {
                        players(isturn).strategy.execute("rolldice")
                        val throwDices = playerController.wuerfeln
                        if (throwDices._3) {
                            // bei gewuerfelten pasch kommt man raus und moved
                            players = players.updated(isturn, players(isturn).resetJailCount)
                            notifyObservers(playerIsFreeEvent(players(isturn)))
                            playerController.movePlayer(throwDices._1 + throwDices._2)
                        } else {
                            //sonst jailcount +1
                            players = players.updated(isturn, players(isturn).incJailTime)
                            // wenn man 3 runden im jail ist kommt man raus, zahlt und moved
                            if (players(isturn).jailCount == 3) {
                                players = players.updated(isturn, players(isturn).resetJailCount)
                                players = players.updated(isturn, players(isturn).decMoney(200))
                                //playerController.checkDept(-1) // owner is bank
                                notifyObservers(playerIsFreeEvent(players(isturn)))
                                playerController.movePlayer(throwDices._1 + throwDices._2)
                            } else notifyObservers(playerRemainsInJailEvent(players(isturn)))
                        }
                    }
                }
            }
        }
    }


    object GameStates {

        var runState = initGameState

        def handle(e: GameStateEvent): Any = {
            e match {
                case e: InitGameEvent => runState = initGameState
                case e: getPlayersEvent => runState = getPlayersState(e)
                case e: rollForPositionsEvent => runState = rollForPositionsState
                case e: createBoardAndPlayersEvent => runState = createBoardAndPlayersState(e)
                case e: runRoundEvent => runState = runRoundState
                case e: checkGameOverEvent => runState = checkGameOverState
                case e: gameOverEvent => runState = gameOverState
            }
            runState
        }

        def getPlayersState(e: getPlayersEvent) = {
            // spieler mit namen einlesensr
            for (i <- 0 until e.playerCount.toInt) {
                isturn = i
                println("Enter name player" + (isturn + 1) + ":")
                notifyObservers(OpenGetNameDialogEvent(currentStage, (isturn + 1))) // adds player in tui/gui... dialog
            }
            for (i <- 0 until e.npcCount.toInt) {
                npcNames = npcNames :+ "NPC " + (i + 1)
            }
            //todo
            // notifyObservers(askUndoGetPlayersEvent())
            //      if (answer == "yes") {
            //        undoManager.undoStep
            //      }
        }

        def rollForPositionsState = {
            println("rollforposstate")
            for (i <- 0 until playerCount) {
                // jeden einmal wuerfeln lassen
                isturn = i
                players(isturn).strategy.execute("rollForPosition") match {
                    case (roll1: Int, roll2: Int, pasch: Boolean) => println(roll1, roll2, pasch)
                        //ergebnis speichern für jeden spieler
                        players = players.updated(i, players(i).setRollForPosition(roll1 + roll2))
                }
            }
            //nach reihenfolge sortieren
            players = players.sortBy(-_.rollForPosition) // - für reversed
            //reihenfolge erstmalig festlegen
            println("spieler mit reihenfolge:")
            for (i <- 0 until playerCount) {
                players = players.updated(i, players(i).setTurnPosition(i))
                println(players(i))
            }
            // Spieler suchen die das gleiche gewuerfelt haben
            var rolledSame: Vector[Player] = Vector[Player]()
            for (i <- 0 until playerCount) {
                for (j <- (i + 1) until playerCount) {
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
        }

        def createBoardAndPlayersState(e: createBoardAndPlayersEvent) = {
            println("createboardandplayersState")
            players = playerController.createPlayers(e.playerNames, e.npcNames)
            board = boardController.createBoard
            val stackpane = currentStage.scene().lookup("#stackpane").asInstanceOf[javafx.scene.layout.StackPane]
            val timer: Timer = new Timer()
            print("yo")
            isturn = 0
            for (i <- 0 until playerCount) {
                stackpane.getChildren().add(players(i).figure)
                movePlayerGui(players(isturn).figure, 350, 350)
                isturn += 1


                //                timer.schedule(new TimerTask {
                //                    override def run(): Unit = {
                //                        movePlayerGui(players(isturn).figure, 350, 350)
                //                        timer.cancel()
                //                    }
                //                }, 1000, 100)
            }
            //todo notifyObservers(e: GuiPutPlayersOnTheBoardEvent)
        }

        def runRoundState: Unit = {
            println("runroundstate")
            // Todo handeln, strassen verkaufen,hypothek bezahlen etc vor dem wuerfeln
            // Todo und nach dem wuerfeln falls er nicht pleite ist
            // todo if player has money raus und einfach so rbüer
            notifyObservers(newRoundEvent(round))
            for (i <- 0 until playerCount) {
                //todo before turn trade or so by pressing buttons -> players(isturn).strategy.execute(onbutton)

                isturn = i
                PlayerTurnStrategy.executePlayerTurn // zug ausfuehren
                //todo end turn by pressing endturnbutton -> players(isturn).strategy.execute(endturn)
            }
            // Rundenende
            round += 1
            notifyObservers(endRoundEvent(round))
            notifyObservers(printEverythingEvent())
        }

        def checkGameOverState = {
            if (round == 10) gameOver = true
            // todo try if (round == 5) runState = gameOverState
            //if (checkGameOver())
            //handle(gameOverEvent())
        }

        def gameOverState = {
            humanPlayers = 0
            npcPlayers = 0
            playerCount = 0
            board = Vector[Cell]()
            players = Vector[Player]()
            playerNames = Vector[String]()
            npcNames = Vector[String]()
            round = 1
            answer = ""
            gameOver = false // todo wie über states
            println("gameover")
            // todo nach dem game sollte eig wieder initstate sein dort sollte alles wieder
            // todo zurueck gesetzt werden aber bugt noch

            //notifyObservers(printEverythingEvent())
            //notifyObservers(gameFinishedEvent(players(gameOver._2)))

            ////////////Try Add label to stackpane ////////////////

            val hatFigureImage = new ImageView(new Image("file:images/Hat.jpg", 800, 800,
                true,
                true))
            val stackpane = currentStage.scene().lookup("#stackpane").asInstanceOf[javafx.scene.layout.StackPane]
            stackpane.getChildren().add(hatFigureImage)

            ////////////////////////////////
        }

        def initGameState = {
            print("InitState")
            //            humanPlayers = 0
            //            npcPlayers = 0
            //            playerCount = 0
            //            board = Vector[Cell]()
            //            players = Vector[Player]()
            //            playerNames = Vector[String]()
            //            npcNames = Vector[String]()
            //            round = 1
            //            answer = ""
            //            gameOver = false // todo wie über states
        }

    }


}
