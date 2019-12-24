package model

trait DiceInterface {
  def roll:Int
  def checkPash(x:Int,y:Int):Boolean
}
