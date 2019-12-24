package model

case class Dice() extends DiceInterface {
    def roll: Int =  1 + (scala.math.random() * 6).toInt
    def checkPash(x : Int,y : Int) : Boolean = x == y
}
