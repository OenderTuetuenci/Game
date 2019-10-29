package model

abstract class Cell(name: String, number: Int){

  def onPlayerEntered(enteredPlayer:Int): String ={
    "\nplayer entered the field\n"
  }

  override def toString: String = name + ' ' + number
}

case class Street(name: String, number: Int, price: Int, owner: Int, rent: Int, home:Int) extends Cell(name,number) {
  override def onPlayerEntered(enteredPlayer:Int): String = {
    println("\nplayer entered " + this.name + ". owner: " + this.owner)
    if (this.owner == -1) "buy"
    else if(this.owner == enteredPlayer) "buy home"
    else "pay"

  }
  def setOwner(x:Int) : Street = Street(name,number,price,x,rent,home)
  //Functions to buy or sell homes to increase rent
  def buyHome(x:Int) : Street ={
    val newRent = rent+(home * 200)
    Street(name,number,price,owner,newRent,home+x)
  }
  def sellHome(x:Int) : Street={
    val newRent = rent-(x*200)
    Street(name,number,price,owner,newRent,home-x)
  }

  override def toString: String = {
    if(home > 0)
      name + ' ' + number + ' ' + price + ' ' + rent + " homecount: "+home
    else
      name + ' ' + number + ' ' + price + ' ' + rent
  }

}

case class Eventcell(name: String,number: Int) extends Cell(name,number) {
  override def onPlayerEntered(enteredPlayer:Int): String = {
    "\nplayer entered an event"
  }

  def drawEventCard(): Unit = {
    println("draw a card")
  }
}




