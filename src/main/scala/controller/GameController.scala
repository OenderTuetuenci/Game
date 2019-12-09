package controller


import model._
import scalafx.application.JFXApp.PrimaryStage
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
    var npcNames: Vector[String] = Vector[String]()
    var isturn = 0 // aktueller spieler
    var round = 1
    var answer = ""
    var currentStage = new PrimaryStage()


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


    def onQuit() = {
        DialogStrategy.open(currentStage, "confirmQuit")
    }

    def onInformation() = {
        DialogStrategy.open(currentStage, "information")
    }


    def onStartGame() = {
        DialogStrategy.open(currentStage, "getPlayers") match {
            case tuple@(p: String, npc: String) =>
                GameStates.handle(getPlayersEvent(p, npc))
        }
        GameStates.handle(createBoardAndPlayersEvent(playerNames, npcNames))
        notifyObservers(printEverythingEvent())
        notifyObservers(OpenGameWindow())
        notifyObservers(displayRollForPositionsEvent())
        GameStates.handle(rollForPositionsEvent())
    }

    object GameStates {

        var runState = initState

        def handle(e: GameStateEvent): Any = {
            e match {
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
                println("Enter name player" + (i + 1) + ":")
                val result = DialogStrategy.open(currentStage, "getPlayerName").toString
                playerNames = playerNames :+ result
            }
            for (i <- 0 until e.npcCount.toInt) {
                npcNames = npcNames :+ "NPC " + (i + 1)
            }

        }

        def rollForPositionsState = {
            println("rollforposstate")
            for (i <- 0 until playerCount) {
                // jeden einmal wuerfeln lassen
                isturn = i
                players(isturn).strategy.execute("rollDice") match {
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

            //      notifyObservers(askUndoGetPlayersEvent())
            //      if (answer == "yes") {
            //        undoManager.undoStep
            //      }
        }

        def runRoundState: Unit = {
            println("runround")

            //notifyObservers(newRoundEvent(round))
            //runRound
            //notifyObservers(printEverythingEvent())
            //notifyObservers(endRoundEvent(round))
        }

        def checkGameOverState = {
            //if (checkGameOver())
            //handle(gameOverEvent())
        }

        def gameOverState = {
            println("gameover")
            //notifyObservers(printEverythingEvent())
            //notifyObservers(gameFinishedEvent(players(gameOver._2)))
        }

        def initState() = print("InitState")

    }


}
