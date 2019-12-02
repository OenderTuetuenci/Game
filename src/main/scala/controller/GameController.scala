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
    var gameOver = false
    var board: Vector[Cell] = Vector[Cell]()
    var players: Vector[Player] = Vector[Player]()
    var isturn = 0 // aktueller spieler
    var round = 1

    def createGame(playerNames:Array[String],npcNames:Array[String]): Unit ={
        players = playerController.createPlayers(playerNames,npcNames)
        board = boardController.createBoard
        humanPlayers = playerNames.length
        npcPlayers = npcNames.length
        playerCount = humanPlayers + npcPlayers
    }
    def run(): Unit = {
        GameStates.runState
    }

    def runRound :Unit ={
        for(i<- 0 until playerCount){
            val roll = rollDice
            players.updated(i,playerController.movePlayer(roll._1,roll._2,players(i)))
            val option = board(players(i).position).onPlayerEntered(i)
            //Todo Jetzt zwischen Human oder Npc endscheiden (vllt indem man im player ein Boolean hat für Npc)
        }
    }
    def rollDice :(Int,Boolean)={
        var jailtime = false
        var paschcount = 0
        var sum = 0
        var rolls = 1
        while (rolls != 0){
            val roll1 = dice.roll
            val roll2 = dice.roll
            sum += roll1+roll2
            if(dice.checkPash(roll1,roll2)){
                paschcount += 1
                rolls +=1
            }
            rolls -=1
        }
        if(paschcount == 3)
            jailtime = true
        (sum,jailtime)
    }
    object GameStates {

        var runState = beforeGameStarts

        def handle(e: GameStateEvent) = {
            e match {
                case e: beforeGameStartsEvent => runState = beforeGameStarts // rollfor
                //case e: rollForPositionsEvent => runState = rollForPositionsState // createplayers
                case e: runRoundEvent => runState = runRoundState // checkgameover
                case e: checkGameOverEvent => runState = checkGameOverState // runround or gameoverstate
                case e: gameOverEvent => runState = gameOverState // end
            }
        }

        def beforeGameStarts() = {
            notifyObservers(newGameEvent())
            runState = runRoundState
        }

        def rollForPositionsState = {
            //notifyObservers(beforeStart)
            runState = runRoundState
        }

        def runRoundState = {
            notifyObservers(newRoundEvent(round))
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
