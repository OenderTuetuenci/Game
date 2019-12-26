package controller

import controller.controllerComponent.{BoardController, PlayerController}
import model.{Cell, DiceInterface, PlayerInterface, PrintEvent}
import scalafx.application.JFXApp.PrimaryStage
import util.{Observable, UndoManager}

trait GameControllerInterface extends Observable{
  val dice:DiceInterface
  val undoManager: UndoManager
  val playerController:PlayerController
  val boardController:BoardController
  var humanPlayers:Int
  var npcPlayers:Int
  var board:Vector[Cell]
  var players:Vector[PlayerInterface]
  var playerNames:Vector[String]
  var remainingFiguresToPick:List[String]
  var playerFigures:Vector[String]
  var npcNames : Vector[String]
  var round:Int
  var answer:String
  var currentStage:PrimaryStage
  var currentPlayer:Int
  var paschCount:Int
  val goXY:(Int,Int)
  val jailXY:(Int,Int)
  val GoToJailXy:(Int,Int)
  val fieldCoordsX:List[Double]
  val fieldCoordsY:List[Double]
  var collectedTax:Int
  var tmpHumanPlayers:Int
  var tmpNpcPlayers:Int
  var tmpBoard:Vector[Cell]
  var tmpPlayers:Vector[PlayerInterface]
  var tmpRound:Int
  var tmpCurrentPlayer:Int
  var tmpCollectedTax:Int

  def createGame(playerNames:Vector[String],npcNames:Vector[String]):Unit
  def checkGameOver():Boolean
  def payRent :Unit
  def buy :Unit
  def buyHome:Unit
  def printFun(e:PrintEvent):Unit
  def checkDepth(player:PlayerInterface,ownerIdx:Int):Unit
  def onQuit():Unit
  def onInformation():Unit
  def onSaveGame():Unit
  def onLoadGame():Unit
  def onStartGame():Unit
  def onRollDice():Unit
  def onEndTurn():Unit
}
