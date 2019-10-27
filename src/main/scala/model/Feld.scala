package model

abstract class Feld(name: String,number: Int){

  def onPlayerEntered(): String ={
    "\nplayer entered the field\n"
  }

  override def toString: String = name + ' ' + number
}

case class Strasse(name: String,number: Int,preis: Int,besitzer: String,miete: Int) extends Feld(name,number) {
  override def onPlayerEntered(): String = {
    println("\nplayer entered " + this.name + ". owner: " + this.besitzer)
    if (this.besitzer == "") "buy"
    else "pay"

  }

  def getPreis: Int = preis

  def getMiete: Int = miete

  def getOwner: String = besitzer

  def setOwner(x:String) : Strasse = Strasse(name,number,preis,x,miete)

  override def toString: String = {
    name + ' ' + number + ' ' + preis + ' ' + miete + " Owner: " + besitzer
  }

}

case class Ereignis(name: String,number: Int) extends Feld(name,number) {
  override def onPlayerEntered(): String = {
    "\nplayer entered an event"
  }

  def drawEventCard(): Unit = {
    println("draw a card")
  }
}




