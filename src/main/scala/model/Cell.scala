package model

import play.api.libs.json._

trait Cell {
    val name: String
    val group: Int
    val image:String
    def onPlayerEntered(enteredPlayer: Int): String
}

trait Buyable extends Cell {
    val mortgage: Boolean
    val price: Int
    val rent: Int
    val owner: Int
    val homecount: Int
    val image: String
    def setOwner(x: Int): Buyable

    def payMortgage(): Buyable

    def getMortgage(): Buyable
}

case class Street(name: String, group: Int, price: Int, owner: Int, rent: Int, homecount: Int, mortgage: Boolean, image: String) extends Buyable {
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered " + this.name + ". owner: " + this.owner)
        if (this.owner == -1) "buy"
        else if (this.owner == enteredPlayer) "buy home"
        else "pay"
    }

    override def setOwner(x: Int): Buyable = Street(name, group, price, x, rent, homecount, mortgage, image)

    override def getMortgage: Buyable = Street(name, group, price, owner, rent, homecount, mortgage = true, image)

    override def payMortgage: Buyable = Street(name, group, price, owner, rent, homecount, mortgage = false, image)

    //Functions to buy or sell homes to increase rent
    def buyHome(x: Int): Street = {
        val newRent = rent + (homecount * 200)
        Street(name, group, price, owner, newRent, homecount + x, mortgage, image)
    }

    def sellHome(x: Int): Street = {
        val newRent = rent - (x * 200)
        Street(name, group, price, owner, newRent, homecount - x, mortgage, image)
    }

    override def toString: String = name + " group: " + group + " price: " + price + " rent: " + rent + " homecount: " + homecount + " mortgage: " + mortgage
}
object Street{
    implicit val streetWrites = Json.writes[Street]
    implicit val streetReads = Json.reads[Street]
}

case class Eventcell(name: String, group: Int,image:String) extends Cell {
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
object Eventcell{
    implicit val eventCellWrites = Json.writes[Eventcell]
    implicit val eventCellReads = Json.reads[Eventcell]
}

case class CommunityChest(name: String, group: Int,image:String) extends Cell {
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
object CommunityChest{
    implicit val communityWrites = Json.writes[CommunityChest]
    implicit val communityReads = Json.reads[CommunityChest]
}

case class Los(name: String, group: Int, image: String) extends Cell {
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered start")
        "\nplayer entered start"
    }

    override def toString: String = {
        name
    }
}
object Los{
    implicit val losWrites = Json.writes[Los]
    implicit val losReads = Json.reads[Los]
}

case class IncomeTax(name: String, group: Int, image: String) extends Cell {
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered IncomeTax")
        "\nplayer entered IncomeTax"
    }

    override def toString: String = {
        name
    }
}
object IncomeTax{
    implicit val incomeWrites = Json.writes[IncomeTax]
    implicit val incomeReads = Json.reads[IncomeTax]
}

case class GoToJail(name: String, group: Int, image: String) extends Cell {
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered go to jail")
        "\nplayer entered GoToJail"
    }

    override def toString: String = {
        name
    }
}
object GoToJail{
    implicit val toJailWrites = Json.writes[GoToJail]
    implicit val toJailReads = Json.reads[GoToJail]
}

case class Jail(name: String, group: Int, image: String) extends Cell {
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered jail")
        "\nplayer entered jail"
    }

    override def toString: String = {
        name
    }
}
object Jail{
    implicit val jailWrites = Json.writes[Jail]
    implicit val jailReads = Json.reads[Jail]
}

case class FreiParken(name: String, group: Int, image: String) extends Cell {
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered FreiParken")
        "\nplayer entered FreiParken"
    }

    override def toString: String = {
        name
    }
}
object FreiParken{
    implicit val freeParkingWrites = Json.writes[FreiParken]
    implicit val freeParkingReads = Json.reads[FreiParken]
}

case class Zusatzsteuer(name: String, group: Int, image: String) extends Cell {
    override def onPlayerEntered(enteredPlayer: Int): String = {
        println("\nplayer entered Zusatzsteuer")
        "\nplayer entered Zusatzsteuer"
    }

    override def toString: String = {
        name
    }
}
object Zusatzsteuer{
    implicit val zusatzWrites = Json.writes[Zusatzsteuer]
    implicit val zusatzReads = Json.reads[Zusatzsteuer]
}