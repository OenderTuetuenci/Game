package model

abstract class Cell(name: String){
  def onPlayerEntered(enteredPlayer:Int): String ={
    "\nplayer entered the field\n"
  }

  override def toString: String = name
}

case class Street(name: String, group: Int, price: Int, owner: Int, rent: Int, home: Int, hypothek: Boolean) extends Cell(name) {
  override def onPlayerEntered(enteredPlayer:Int): String = {
    println("\nplayer entered " + this.name + ". owner: " + this.owner)
    if (this.owner == -1) "buy"
    else if(this.owner == enteredPlayer) "buy home"
    else "pay"

  }

  def setOwner(x: Int): Street = Street(name, group, price, x, rent, home, hypothek)
  //Functions to buy or sell homes to increase rent
  def buyHome(x:Int) : Street ={
    val newRent = rent+(home * 200)
    Street(name, group, price, owner, newRent, home + x, hypothek)
  }
  def sellHome(x:Int) : Street={
    val newRent = rent-(x*200)
    Street(name, group, price, owner, newRent, home - x, hypothek)
  }

  override def toString: String = {
    if(home > 0)
      name + " group: " + group + " price: " + price + " rent: " + rent + " homecount: " + home + " hypothek: " + hypothek
    else
      name + " group: " + group + " price: " + price + " rent: " + rent + " hypothek: " + hypothek
  }
}

case class Eventcell(name: String) extends Cell(name) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered an event")
    "\nplayer entered an event"
  }

  def drawEventCard(): Unit = {
    println("draw a card")
  }

    override def toString: String = {
        name
    }
}


case class CommunityChest(name: String) extends Cell(name) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered CommunityChest")
    "\nplayer entered CommunityChest"
  }

  def drawEventCard(): Unit = {
    println("draw a card")
  }

    override def toString: String = {
        name
    }
}

case class IncomeTax(name: String) extends Cell(name) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered IncomeTax")
    "\nplayer entered IncomeTax"
  }

    override def toString: String = {
        name
    }
}

case class Elektrizitaetswerk(name: String, group: Int, price: Int, owner: Int, rent: Int, hypothek: Boolean) extends Cell(name) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered JailVisit")
    "\nplayer entered JailVisit"
  }

  def setOwner(x: Int): Elektrizitaetswerk = Elektrizitaetswerk(name, group, price, x, rent, hypothek)


  override def toString: String = {
      name + " group: " + group + " price: " + price + " rent: " + rent + " hypothek: " + hypothek
    }
}


case class Trainstation(name: String, group: Int, price: Int, owner: Int, rent: Int, hypothek: Boolean) extends Cell(name) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered " + name)
    "\nplayer entered SouthTrainstation"
  }

  def setOwner(x: Int): Trainstation = Trainstation(name, group, price, x, rent, hypothek)

    override def toString: String = {
      name + " group: " + group + " price: " + price + " rent: " + rent + " hypothek: " + hypothek
    }
}

case class Los(name: String) extends Cell(name) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered start")
    "\nplayer entered start"
  }

    override def toString: String = {
        name
    }
}

case class GoToJail(name: String) extends Cell(name) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered jail")
    "\nplayer entered GoToJail"
  }

    override def toString: String = {
        name
    }
}

case class Wasserwerk(name: String, group: Int, price: Int, owner: Int, rent: Int, hypothek: Boolean) extends Cell(name) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered Wasserwerk")
    "\nplayer entered Wasserwerk"
  }

    override def toString: String = {
      name + " group: " + group + " price: " + price + " rent: " + rent + " hypothek: " + hypothek
    }
}

case class Jail(name: String) extends Cell(name) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered jail")
    "\nplayer entered jail"
  }

    override def toString: String = {
        name
    }
}

case class FreiParken(name: String) extends Cell(name) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered FreiParken")
    "\nplayer entered FreiParken"
  }

    override def toString: String = {
        name
    }
}

case class Zusatzsteuer(name: String) extends Cell(name) {
  override def onPlayerEntered(enteredPlayer: Int): String = {
    println("\nplayer entered Zusatzsteuer")
    "\nplayer entered Zusatzsteuer"
  }

    override def toString: String = {
        name
    }
}