package model.playerComponent

import com.google.inject.Inject
import com.google.inject.name.Named
import model.PlayerInterface
import play.api.libs.json.Json

case class Player @Inject()(@Named("name") name: String, @Named("position") position: Int = 0, @Named("money") money: Int = 2000, @Named("jailCount") jailCount: Int = 0,
                            @Named("turnPosition") turnPosition: Int = 0, @Named("rollForPosition") rollForPosition: Int = 0, @Named("figure") figure: String
                            , @Named("isNpc") isNpc: Boolean = false) extends PlayerInterface {
    override def toString: String = {
        "name: " + this.name + " pos: " + this.position + " money: " + this.money + " roundsInJail: " + this.jailCount + " rollForPosition: " + this.rollForPosition + " turnPosition: " + this.turnPosition
    }

    def move(x: Int): Player = this.copy(position = this.position + x)

    def moveBack(x: Int): Player = this.copy(position = this.position - x)

    def incJailTime: Player = this.copy(jailCount = this.jailCount + 1)

    def incMoney(x: Int): Player = this.copy(money = this.money + x)

    def decMoney(x: Int): Player = this.copy(money = this.money - x)

    def resetJailCount: Player = this.copy(jailCount = 0)

    def moveToStart: Player = this.copy(position = 0)

    def moveToJail: Player = this.copy(position = 10, jailCount = 0)

    def setRollForPosition(x: Int): Player = this.copy(rollForPosition = x)

    def setTurnPosition(x: Int): Player = this.copy(turnPosition = x)

    def setFigure(imgPath: String): Player = this.copy(figure = imgPath)

    override def setName(x: String): Player = this.copy(name = x)
}
object Player{
    implicit val playerWrites = Json.writes[Player]
    implicit val playerReads = Json.reads[Player]
}

