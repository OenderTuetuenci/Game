package model

case class Dice(){
  def throwDice(): Int = 1+(scala.math.random()*6).toInt
  def checkPash(x : Int,y : Int) : Boolean = x == y
}
