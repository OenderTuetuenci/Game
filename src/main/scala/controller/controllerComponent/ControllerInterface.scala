package controller.controllerComponent

import model._
import scalafx.application.JFXApp.PrimaryStage
import util.{Observable, UndoManager}

trait ControllerInterface extends Observable {
    val cards: CardsInterface
    val chanceCardsList: List[String]
    val communityChestCardsList: List[String]
    val dice: DiceInterface
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

    def payRent(field: Buyable): Unit

    def payTax: Unit

    def payElse: Unit

    def buyHome: Unit

    def onQuit(): Unit

    def onInformation(): Unit

    def onSaveGame(): Unit

    def onLoadGame(): Unit

    def onStartGame(): Unit

    def runNewGame(): Unit

    def onRollDice(): Unit

    def onEndTurn(): Unit

    def checkPlayerDept(ownerIdx: Int): Unit

    // from boardcontroller

    def newOwner(playerNr: Int, cell: Cell): Cell

    def createBoard: Vector[Cell]

    def activateStart(field: Los): Unit

    def activateStreet(field: Buyable): Unit

    def activateIncomeTax(field: IncomeTax): Unit

    def activateVisitJail(field: Jail): Unit

    def activateFreiParken(field: FreiParken): Unit

    def activateLuxuaryTax(field: Zusatzsteuer): Unit

    def activateChance(field: Eventcell): Unit

    def activateCommunityChest(field: CommunityChest): Unit

    def activateJail(field: GoToJail): Unit

    // from playercontroller

    def createPlayers(playerNames: Vector[String], npcNames: Vector[String]): Vector[PlayerInterface]

    def buy(field: Buyable): Unit

    def wuerfeln: (Int, Int, Boolean)

    def movePlayer(sumDiceThrow: Int): Vector[PlayerInterface]
}