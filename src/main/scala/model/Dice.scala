package model

case class Dice(eyeCount: Int = 0) {
    def roll: Dice = Dice(1 + (scala.math.random() * 6).toInt)
    def checkPash(x : Int,y : Int) : Boolean = x == y
}
