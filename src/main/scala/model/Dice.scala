package model

case class Dice() {
  def throwDice: Int = 1+(scala.math.random()*6).toInt
  def throwDice(predef:Int): Int = predef
  def checkPash(x : Int,y : Int) : Boolean = x == y
}
