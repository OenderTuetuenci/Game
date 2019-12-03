package controller

;


import model._
import util.Observable;
import scala.io.StdIn._

class GameController extends Observable {
    val dice = Dice()
    val playerController = new PlayerController(this)
    val boardController = new BoardController(this)
    var humanPlayers = 0
    var npcPlayers = 0
    var playerCount = 0
    var gameOver = false
    var board: Vector[Cell] = Vector[Cell]()
    var players: Vector[Player] = Vector[Player]()
    var isturn = 0 // aktueller spieler
    var round = 1
    var answer = ""

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
            isturn = i
            val roll = rollDice
            if(roll._2)
                players = players.updated(i,players(i).moveToJail)
            else {
                players = playerController.movePlayer(roll._1)
                val option = board(players(i).position).onPlayerEntered(i)
                HumanOrNpcStrategy.selectStrategy(players(i).isNpc, option)
                HumanOrNpcStrategy.strategy
            }
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

    def payRent :Unit ={
        val updated = playerController.payRent(board(players(isturn).position).asInstanceOf[Buyable])
        players = updated._2
        board = updated._1
        playerCount = updated._3
    }
    def buyStreet : Unit={
        val updated = playerController.buyStreet(board(players(isturn).position).asInstanceOf[Street])
        board = updated._1
        players = updated._2
    }
    def print (e:PrintEvent): Unit ={
        notifyObservers(e)
    }
    object HumanOrNpcStrategy{
        var option = "buy"
        var strategy = human(option)
        def selectStrategy(isNpc:Boolean,option: String):Unit={
            if(isNpc)
                strategy = npc(option)
            else
                strategy = human(option)
        }
        def human(option:String):Unit = {
            if(option == "buy"){
                notifyObservers(askBuyEvent())
                notifyObservers(answerEvent())
                if(answer == "yes"){
                    buyStreet
                }
            }
            else if(option == "pay"){
                payRent
            }
        }
        def npc(option:String):Unit = {
            if(option == "buy"){
                buyStreet
            }
            else if(option == "pay"){
                payRent
            }
        }
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

        def beforeGameStarts(): Unit = {
            notifyObservers(newGameEvent())
            runState = runRoundState
        }

        def rollForPositionsState:Unit = {
            //notifyObservers(beforeStart)
            runState = runRoundState
        }

        def runRoundState:Unit = {
            notifyObservers(newRoundEvent(round))
            runRound
            notifyObservers(endRoundEvent(round))
            runState = checkGameOverState
        }

        def checkGameOverState: Unit = if (gameOver) runState = gameOverState else runState = runRoundState

        def gameOverState = {
            //notifyObservers(beforeStart)
        }

    }


}
