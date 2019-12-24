package model

import controller.HumanOrNpcStrategy
import scalafx.scene.image.ImageView

trait PlayerInterface {
  val name:String
  val position:Int
  val money:Int
  val jailCount:Int
  val ownedStreets:Vector[Int]
  val turnPosition:Int
  val rollForPosition:Int
  val strategy:HumanOrNpcStrategy
  val figure:ImageView
  def toString :String
  def move(x: Int): Player
  def buyStreet(streetNr: Int): Player
  def sellStreet(streetNr: Int): Player
  def moveBack(x: Int): Player
  def incJailTime: Player
  def incMoney(x: Int): Player
  def decMoney(x: Int): Player
  def resetJailCount: Player
  def moveToStart: Player
  def moveToJail: Player
  def setRollForPosition(x: Int): Player
  def setTurnPosition(x: Int): Player
  def setFigure(imgView: ImageView): Player
}
