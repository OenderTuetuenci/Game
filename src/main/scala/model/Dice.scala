package model

case class Dice(eyecount:Int = 1+(scala.math.random()*6).toInt){
  def throwDice(): Dice = Dice()
  override def toString: String = eyecount.toString
  def checkPash(aDice : Dice) : Boolean = aDice.eyecount == this.eyecount
}
