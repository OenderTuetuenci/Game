package controller

;


import model._
import util.Observable;

class GameController extends Observable {
    val dice = Dice()
    val playerController = new PlayerController()
    val boardController = new BoardController()
    var humanPlayers = 0
    var npcPlayers = 0
    var playerCount = 0
    var playerNames: Array[String] = Array[String]()
    var gameOver = false
    var npcNames: Array[String] = Array[String]()
    var board: Vector[Cell] = Vector[Cell]()
    var players: Vector[Player] = Vector[Player]()
    var isturn = 0 // aktueller spieler
    var round = 1

    def run(): Unit = {
        GameStates.runState
    }

    object GameStates {

        var runState = beforeGameStarts

        def handle(e: GameStateEvent) = {
            e match {
                case e: beforeGameStartsEvent => runState = beforeGameStarts // rollfor
                case e: rollForPositionsEvent => runState = rollForPositionsState // createplayers
                case e: createPlayersEvent => runState = createPlayersState // createboard
                case e: createBoardEvent => runState = createBoardState //runround
                case e: runRoundEvent => runState = runRoundState // checkgameover
                case e: checkGameOverEvent => runState = checkGameOverState // runround or gameoverstate
                case e: gameOverEvent => runState = gameOverState // end
            }
        }

        def beforeGameStarts = {
            //notifyObservers(beforeStart)
            runState = rollForPositionsState
        }

        def rollForPositionsState = {
            //notifyObservers(beforeStart)
            runState = createPlayersState
        }

        def createPlayersState = {
            //notifyObservers(beforeStart)
            runState = createBoardState
        }

        def createBoardState = {
            //notifyObservers(beforeStart)
            runState = runRoundState
        }

        def runRoundState = {
            //notifyObservers(beforeStart)
            runState = checkGameOverState
        }

        def checkGameOverState = {
            //notifyObservers(beforeStart)
            if (gameOver) runState = gameOverState
            else runState = runRoundState
        }

        def gameOverState = {
            //notifyObservers(beforeStart)
        }

    }

}
