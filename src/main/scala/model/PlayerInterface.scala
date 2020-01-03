package model

import model.playerComponent.Player

trait PlayerInterface {
  val name:String
  val position:Int
  val money:Int
  val jailCount:Int
  val turnPosition:Int
  val rollForPosition: Int
  val figure: String
  def toString :String
  def move(x: Int): Player
  def moveBack(x: Int): Player
  def incJailTime: Player
  def incMoney(x: Int): Player
  def decMoney(x: Int): Player
  def resetJailCount: Player
  def moveToStart: Player
  def moveToJail: Player
  def setRollForPosition(x: Int): Player

  def setTurnPosition(x: Int): Player

  def setFigure(figure: String): Player
}
