package controller


import model._
import util.{Observable, UndoManager}

import scala.io.StdIn._

class GameController extends Observable {
  val dice = Dice()
  val undoManager = new UndoManager
  val playerController = new PlayerController(this)
  val boardController = new BoardController(this)
  var humanPlayers = 0
  var npcPlayers = 0
  var board: Vector[Cell] = Vector[Cell]()
  var players: Vector[Player] = Vector[Player]()
  var isturn = 0 // aktueller spieler
  var round = 1
  var answer = ""

  def createGame(playerNames: Array[String], npcNames: Array[String]): Unit = {
    players = playerController.createPlayers(playerNames, npcNames)
    board = boardController.createBoard
    humanPlayers = playerNames.length
    npcPlayers = npcNames.length
    notifyObservers(askUndoGetPlayersEvent())
    if (answer == "yes") {
      undoManager.undoStep
    }
  }

  def beforeGameStarts() = {
    print("yo")
    //GameStates.handle(runRoundEvent())
    //GameStates.handle(checkGameOverEvent())
    //if (gameOver._1)
    //  GameStates.handle(gameOverEvent())
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

  def run(): Unit = {
    GameStates.handle(rollForPositionsEvent())
    GameStates.handle(createBoardAndPlayersEvent())
    do {
      GameStates.handle(runRoundEvent())
      GameStates.handle(checkGameOverEvent())
    } while (!GameStates.runState.equals(gameOverEvent()))
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

  object GameStates {

    var runState = getPlayersState

    def handle(e: GameStateEvent) = {
      e match {
        case e: rollForPositionsEvent => runState = rollForPositionsState
        case e: createBoardAndPlayersEvent => runState = createBoardAndPlayersState
        case e: runRoundEvent => runState = runRoundState
        case e: checkGameOverEvent => runState = checkGameOverState
        case e: gameOverEvent => runState = gameOverState
      }
      //runState
    }


    def getPlayersState = println("getplayers")

    def rollForPositionsState = println("rollforpositions")

    def createBoardAndPlayersState() = println("createboardandplayers")

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

  }


}
