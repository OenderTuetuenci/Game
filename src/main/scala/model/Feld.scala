package model

abstract class Feld(name: String,number: Int){

  def onPlayerEntered(): String ={
    "\nplayer entered the field\n"
  }

  override def toString: String = name + ' ' + number
}

case class Strasse(name: String,number: Int,price: Int,owner: String,rent: Int,home:Int) extends Feld(name,number) {
  override def onPlayerEntered(): String = {
    println("\nplayer entered " + this.name + ". owner: " + this.owner)
    if (this.owner == "") "buy"
    else "pay"

  }
  def setOwner(x:String) : Strasse = Strasse(name,number,price,x,rent,home)
  //Functions to buy or sell homes to increase rent
  def buyHome(x:Int) : Strasse ={
    val newRent = rent+(home * 200)
    Strasse(name,number,price,owner,newRent,home+x)
  }
  def sellHome(x:Int) : Strasse={
    val newRent = rent-(x*200)
    Strasse(name,number,price,owner,newRent,home-x)
  }

  override def toString: String = {
    if(home > 0)
      name + ' ' + number + ' ' + price + ' ' + rent + " Owner: " + owner +" homecount: "+home
    else
      name + ' ' + number + ' ' + price + ' ' + rent + " Owner: " + owner
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




