package model
abstract class Feld(name: String,number: Int){

  def onPlayerEntered(): Unit ={
    println("player entered the field")
  }

  override def toString: String = name + ' ' + number
}

case class Strasse(name: String,number: Int,preis: Int,besitzer: Player,miete: Int) extends Feld(name,number) {
  override def onPlayerEntered(): Unit = {
    println("player entered a street")
  }

  def getPreis(): Int = preis

  def geMiete(): Int = miete

  override def toString: String = name + ' ' + number + ' ' + preis + ' ' + besitzer + ' ' + miete

}

case class Ereignis(name: String,number: Int) extends Feld(name,number) {
  override def onPlayerEntered(): Unit = {
    println("player entered an event")
  }

  def drawEventCard(): Unit = {
    println("draw a card")
  }
}




