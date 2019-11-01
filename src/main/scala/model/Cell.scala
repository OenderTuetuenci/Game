package model

abstract class Cell(name: String,group: Int){

  def onPlayerEntered(enteredPlayer:Int): String ={
    "\nplayer entered the field\n"
  }

  override def toString: String = name
}

case class Street(name: String, group: Int, price: Int, owner: Int, rent: Int, home:Int) extends Cell(name, group) {
  override def onPlayerEntered(enteredPlayer:Int): String = {
    println("\nplayer entered " + this.name + ". owner: " + this.owner)
    if (this.owner == -1) "buy"
    else if(this.owner == enteredPlayer) "buy home"
    else "pay"

  }
  def setOwner(x:Int) : Street = Street(name,group,price,x,rent,home)
  //Functions to buy or sell homes to increase rent
  def buyHome(x:Int) : Street ={
    val newRent = rent+(home * 200)
    Street(name,group,price,owner,newRent,home+x)
  }
  def sellHome(x:Int) : Street={
    val newRent = rent-(x*200)
    Street(name,group,price,owner,newRent,home-x)
  }

  override def toString: String = {
    if(home > 0)
      name + ' ' + group + ' ' + price + ' ' + rent + " homecount: "+home
    else
      name + ' ' + group + ' ' + price + ' ' + rent
  }
}

case class Eventcell(name: String, group: Int) extends Cell(name, group) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered an event")
    "\nplayer entered an event"
  }

  def drawEventCard(): Unit = {
    println("draw a card")
  }
}

case class CommunityChest(name: String, group: Int) extends Cell(name, group) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered CommunityChest")
    "\nplayer entered CommunityChest"
  }

  def drawEventCard(): Unit = {
    println("draw a card")
  }
}

case class IncomeTax(name: String, group: Int) extends Cell(name, group) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered IncomeTax")
    "\nplayer entered IncomeTax"
  }
}

case class JailVisit(name: String, group: Int) extends Cell(name, group) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered JailVisit")
    "\nplayer entered JailVisit"
  }
}

case class Elektrizitaetswerk(name: String, group: Int,price: Int, owner: Int, rent: Int) extends Cell(name,group) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered JailVisit")
    "\nplayer entered JailVisit"
  }
}


case class SouthTrainstation(name: String,group: Int,price: Int, owner: Int, rent: Int) extends Cell(name,group) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered SouthTrainstation")
    "\nplayer entered SouthTrainstation"
  }
}

case class WestTrainstation(name: String,group: Int,price: Int, owner: Int, rent: Int) extends Cell(name, group) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered WestTrainstation")
    "\nplayer entered WestTrainstation"
  }
}

case class NorthTrainstation(name: String,group: Int,price: Int, owner: Int, rent: Int) extends Cell(name, group) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered NorthTrainstation")
    "\nplayer entered NorthTrainstation"
  }
}

case class MainTrainstation(name: String,group: Int,price: Int, owner: Int, rent: Int) extends Cell(name, group) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered MainTrainstation")
    "\nplayer entered MainTrainstation"
  }
}

case class Los(name: String, group: Int) extends Cell(name, group) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered start")
    "\nplayer entered start"
  }
}

case class GoToJail(name: String, group: Int) extends Cell(name, group) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered jail")
    "\nplayer entered GoToJail"
  }
}

case class Wasserwerk(name: String, group: Int,price: Int, owner: Int, rent: Int) extends Cell(name, group) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered Wasserwerk")
    "\nplayer entered Wasserwerk"
  }
}

case class Jail(name: String, group: Int) extends Cell(name, group) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered jail")
    "\nplayer entered jail"
  }
}

case class FreiParken(name: String, group: Int) extends Cell(name, group) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered FreiParken")
    "\nplayer entered FreiParken"
  }
}

case class Zusatzsteuer(name: String, group: Int) extends Cell(name, group) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered Zusatzsteuer")
    "\nplayer entered Zusatzsteuer"
  }
}