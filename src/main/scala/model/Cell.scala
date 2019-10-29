package model

abstract class Cell(name: String, number: Int){

  def onPlayerEntered(x:Player): String ={
    "\nplayer entered the field\n"
  }

  override def toString: String = name + ' ' + number
}

case class Street(name: String, number: Int, price: Int, owner: String, rent: Int, home:Int) extends Cell(name,number) {
  override def onPlayerEntered(x:Player): String = {
    println("\nplayer entered " + this.name + ". owner: " + this.owner)
    if (this.owner == "") "buy"
    else if(this.owner == x.name) "buy home"
    else "pay"

  }
  def setOwner(x:String) : Street = Street(name,number,price,x,rent,home)
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
      name + ' ' + number + ' ' + price + ' ' + rent + " Owner: " + owner +" homecount: "+home
    else
      name + ' ' + number + ' ' + price + ' ' + rent + " Owner: " + owner
  }

}

case class Eventcell(name: String,number: Int) extends Cell(name,number) {
  override def onPlayerEntered(x:Player): String = {
    "\nplayer entered an event"
  }

  def drawEventCard(): Unit = {
    println("draw a card")
  }
}




