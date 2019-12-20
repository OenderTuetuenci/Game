package model

import scalafx.scene.image.Image

trait Cell {
    val name: String

    def onPlayerEntered(enteredPlayer: Int): String
}

trait Buyable extends Cell {
    val mortgage: Boolean
    val price: Int
    val rent: Int
    val owner: Int
    val image: Image
    def setOwner(x: Int): Buyable

    def payMortgage(): Buyable

    def getMortgage(): Buyable
}

case class Street(name: String, group: Int, price: Int, owner: Int, rent: Int, home: Int, mortgage: Boolean, image: Image) extends Buyable {
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered " + this.name + ". owner: " + this.owner)
        if (this.owner == -1) "buy"
        else if (this.owner == enteredPlayer) "buy home"
        else "pay"
    }

    override def setOwner(x: Int): Buyable = Street(name, group, price, x, rent, home, mortgage, image)

    override def getMortgage: Buyable = Street(name, group, price, owner, rent, home, mortgage = true, image)

    override def payMortgage: Buyable = Street(name, group, price, owner, rent, home, mortgage = false, image)

    //Functions to buy or sell homes to increase rent
    def buyHome(x: Int): Street = {
        val newRent = rent + (home * 200)
        Street(name, group, price, owner, newRent, home + x, mortgage, image)
    }

    def sellHome(x: Int): Street = {
        val newRent = rent - (x * 200)
        Street(name, group, price, owner, newRent, home - x, mortgage, image)
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