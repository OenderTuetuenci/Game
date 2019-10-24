package model

case class Wuerfel(eyecount:Int = 1+(scala.math.random()*6).toInt){

  def throwDice(): Wuerfel = {
    Wuerfel()
  }
}
