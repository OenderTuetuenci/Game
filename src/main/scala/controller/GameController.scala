package controller

import java.util.{Timer, TimerTask}

import model._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.image.{Image, ImageView}
import util.{Observable, UndoManager}

class GameController extends Observable {
    val dice = Dice()
    val undoManager = new UndoManager
    val playerController = new PlayerController(this)
    val boardController = new BoardController(this)
    var humanPlayers = 0
    var npcPlayers = 0
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
    // feldcoords todo resizeable mainwindow offset xy stackpane
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
        notifyObservers(OpenConfirmationDialogEvent())
    }

    def onInformation() = {
        notifyObservers(OpenInformationDialogEvent())
    }




    def onStartGame() = {
        notifyObservers(OpenGetPlayersDialogEvent())
        notifyObservers(ClearGuiElementsEvent())
        notifyObservers(UpdateListViewPlayersEvent())
        GameStates.handle(getPlayersEvent())
        GameStates.handle(createBoardAndPlayersEvent())
        notifyObservers(printEverythingEvent())
        notifyObservers(displayRollForPositionsEvent())
        GameStates.handle(rollForPositionsEvent())
        do {
            GameStates.handle(runRoundEvent())
            GameStates.handle(checkGameOverEvent())
        } while (!gameOver)
        GameStates.handle(gameOverEvent())
        //GameStates.handle(InitGameEvent())

        // todo try} while(!GameStates.runState == GameStates.gameOverState))
    }


    def onRollDice() = {
        //if game running //todo if not runstate == initstate
        notifyObservers(OpenInformationDialogEvent())
    }

    def movePlayerSmooveTimerGui(): Unit = {
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
                case e: createBoardAndPlayersEvent => runState = createBoardAndPlayersState
                case e: runRoundEvent => runState = runRoundState
                case e: checkGameOverEvent => runState = checkGameOverState
                case e: gameOverEvent => runState = gameOverState
            }
            runState
        }

        def getPlayersState(e: getPlayersEvent) = {
            // spieler mit namen einlesensr
            for (i <- 0 until humanPlayers) {
                println("Enter name player" + (isturn + 1) + ":")
                notifyObservers(OpenGetNameDialogEvent(i)) // adds player in tui/gui... dialog
            }
            for (i <- 0 until npcPlayers) {
                npcNames = npcNames :+ "NPC " + (i + 1)
                //todo
            }
            //todo
            // notifyObservers(askUndoGetPlayersEvent())
            //      if (answer == "yes") {
            //        undoManager.undoStep
            //      }
        }

        def rollForPositionsState = {
            println("rollforposstate")
            for (i <- 0 until humanPlayers + npcPlayers) {
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
            for (i <- 0 until humanPlayers + npcPlayers) {
                players = players.updated(i, players(i).setTurnPosition(i))
                println(players(i))
            }
            // Spieler suchen die das gleiche gewuerfelt haben
            var rolledSame: Vector[Player] = Vector[Player]()
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
        }

        def createBoardAndPlayersState = {
            println("createboardandplayersState")
            players = playerController.createPlayers(playerNames, npcNames)
            board = boardController.createBoard
            for (i <- 0 until humanPlayers + npcPlayers) {
                val stackpane = currentStage.scene().lookup("#stackpane").asInstanceOf[javafx.scene.layout.StackPane]
                stackpane.getChildren().add(players(i).figure)
                notifyObservers(MovePlayerFigureEvent(players(i).figure, 350, 350))


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
            for (i <- 0 until humanPlayers + npcPlayers) {
                //todo before turn trade or so by pressing buttons -> players(isturn).strategy.execute(onbutton)

                isturn = i
                PlayerTurnStrategy.executePlayerTurn // zug ausfuehren
                notifyObservers(UpdateListViewPlayersEvent())
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
            notifyObservers(openGameOverDialogEvent())

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
            answer = ""
            gameOver = false // todo wie über states
            // delete everything on the board and board
            val stackpane = currentStage.scene().lookup("#stackpane").asInstanceOf[javafx.scene.layout.StackPane]
            stackpane.getChildren().removeAll()
            // readd board
            stackpane.getChildren().add(new ImageView(new Image("file:images/board.jpg", 800, 800, true, true))
            )
            println("gameover")




            // todo nach dem game sollte eig wieder initstate sein dort sollte alles wieder
            // todo zurueck gesetzt werden aber bugt noch

            //notifyObservers(printEverythingEvent())
            //notifyObservers(gameFinishedEvent(players(gameOver._2)))

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
