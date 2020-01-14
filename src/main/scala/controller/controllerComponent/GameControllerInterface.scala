package controller.controllerComponent

import controller.controllerComponent.controllerBaseImpl.{BoardController, PlayerController}
import model._
import scalafx.application.JFXApp.PrimaryStage
import util.{Observable, UndoManager}

trait GameControllerInterface extends Observable {
    val cards: CardsInterface
    val chanceCardsList: List[String]
    val communityChestCardsList: List[String]
    val dice: DiceInterface
    val playerController: PlayerController
    val boardController: BoardController
    val undoManager: UndoManager
    var chanceCards: Vector[String]
    var communityChestCards: Vector[String]
    var humanPlayers: Int
    var npcPlayers: Int
    var board: Vector[Cell]
    var players: Vector[PlayerInterface]
    var playerNames: Vector[String]
    var remainingFiguresToPick: List[String]
    var playerFigures: Vector[String]
    var npcNames: Vector[String]
    var round: Int
    var answer: String
    var currentStage: PrimaryStage
    var currentPlayer: Int
    var paschCount: Int
    val fieldCoordsX: List[Double]
    val fieldCoordsY: List[Double]
    var collectedTax: Int
    var tmpHumanPlayers: Int
    var tmpNpcPlayers: Int
    var tmpBoard: Vector[Cell]
    var tmpPlayers: Vector[PlayerInterface]
    var tmpRound: Int
    var tmpCurrentPlayer: Int
    var tmpCollectedTax: Int

    def createGame(playerNames: Vector[String], npcNames: Vector[String]): Unit

    def checkGameOver(): Boolean

    def payRent: Unit

    def buy: Unit

    def auction: Unit

    def buyHome: Unit

    def printFun(e: PrintEvent): Unit

    def onQuit(): Unit

    def onInformation(): Unit

    def onSaveGame(): Unit

    def onLoadGame(): Unit

    def onStartGame(): Unit

    def onRollDice(): Unit

    def onEndTurn(): Unit

    def checkPlayerDept(ownerIdx: Int): Unit
}
