package model

trait Cell{
    val name:String
    def onPlayerEntered(enteredPlayer: Int):String
}

trait Buyable extends Cell {
    val mortgage:Boolean
    val price:Int
    val rent:Int
    val owner:Int
    def setOwner(x:Int):Buyable
    def getMortgage():Buyable
}

case class Street(name: String, group: Int, price: Int, owner: Int, rent: Int, home: Int, mortgage: Boolean) extends Buyable{
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered " + this.name + ". owner: " + this.owner)
        if (this.owner == -1) "buy"
        else if (this.owner == enteredPlayer) "buy home"
        else "pay"
    }

    override def setOwner(x: Int): Buyable = Street(name, group, price, x, rent, home, mortgage)

    override def getMortgage: Buyable = Street(name, group, price, owner, rent, home, mortgage = true)

    def payMortgage: Street = Street(name, group, price, owner, rent, home, mortgage = false)

    //Functions to buy or sell homes to increase rent
    def buyHome(x: Int): Street = {
        val newRent = rent + (home * 200)
        Street(name, group, price, owner, newRent, home + x, mortgage)
    }

    def sellHome(x: Int): Street = {
        val newRent = rent - (x * 200)
        Street(name, group, price, owner, newRent, home - x, mortgage)
    }

    override def toString: String = name + " group: " + group + " price: " + price + " rent: " + rent + " homecount: " + home + " mortgage: " + mortgage
}

case class Eventcell(name: String) extends Cell {
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


case class CommunityChest(name: String) extends Cell {
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

case class IncomeTax(name: String) extends Cell {
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered IncomeTax")
        "\nplayer entered IncomeTax"
    }

    override def toString: String = {
        name
    }
}

case class Elektrizitaetswerk(name: String, group: Int, price: Int, owner: Int, rent: Int, hypothek: Boolean) extends Cell {
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered JailVisit")
        "\nplayer entered JailVisit"
    }

    def setOwner(x: Int): Elektrizitaetswerk = Elektrizitaetswerk(name, group, price, x, rent, hypothek)


    override def toString: String = {
        name + " group: " + group + " price: " + price + " rent: " + rent + " hypothek: " + hypothek
    }
}


case class Trainstation(name: String, group: Int, price: Int, owner: Int, rent: Int, mortgage: Boolean) extends Buyable {
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered " + name)
        "\nplayer entered SouthTrainstation"
    }

    def setOwner(x: Int): Trainstation = Trainstation(name, group, price, x, rent, mortgage)

    def getMortgage(): Trainstation = Trainstation(name, group, price, owner, rent, mortgage = true)

    def payHypothek(): Trainstation = Trainstation(name, group, price, owner, rent, mortgage = false)

    override def toString: String = {
        name + " group: " + group + " price: " + price + " rent: " + rent + " mortgage: " + mortgage
    }
}

case class Los(name: String) extends Cell {
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered start")
        "\nplayer entered start"
    }

    override def toString: String = {
        name
    }
}

case class GoToJail(name: String) extends Cell{
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered jail")
        "\nplayer entered GoToJail"
    }

    override def toString: String = {
        name
    }
}

case class Wasserwerk(name: String, group: Int, price: Int, owner: Int, rent: Int, hypothek: Boolean) extends Cell{
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered Wasserwerk")
        "\nplayer entered Wasserwerk"
    }

    override def toString: String = {
        name + " group: " + group + " price: " + price + " rent: " + rent + " hypothek: " + hypothek
    }
}

case class Jail(name: String) extends Cell{
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered jail")
        "\nplayer entered jail"
    }

    override def toString: String = {
        name
    }
}

case class FreiParken(name: String) extends Cell {
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered FreiParken")
        "\nplayer entered FreiParken"
    }

    override def toString: String = {
        name
    }
}

case class Zusatzsteuer(name: String) extends Cell {
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered Zusatzsteuer")
        "\nplayer entered Zusatzsteuer"
    }

    override def toString: String = {
        name
    }
}