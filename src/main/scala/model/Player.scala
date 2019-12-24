package model

import controller.HumanOrNpcStrategy
import scalafx.scene.image.ImageView

case class Player(name: String, position: Int = 0, money: Int = 1000, jailCount: Int = 0,
                  ownedStreets: Vector[Int] = Vector[Int](), turnPosition: Int = 0, rollForPosition: Int = 0, strategy: HumanOrNpcStrategy, figure: ImageView) extends PlayerInterface {
    override def toString: String = {
        "name: " + this.name + " pos: " + this.position + " money: " + this.money + " roundsInJail: " + this.jailCount + " ownedStreets: " + this.ownedStreets.mkString(",") + " rollForPosition: " + this.rollForPosition + " turnPosition: " + this.turnPosition
    }

    def move(x: Int): Player = this.copy(position = this.position + x)

    def buyStreet(streetNr: Int): Player = this.copy(ownedStreets = this.ownedStreets :+ streetNr)

    def sellStreet(streetNr: Int): Player = this.copy(ownedStreets = this.ownedStreets.filterNot(o => o == streetNr))

    def moveBack(x: Int): Player = this.copy(position = this.position - x)

    def incJailTime: Player = this.copy(jailCount = this.jailCount + 1)

    def incMoney(x: Int): Player = this.copy(money = this.money + x)

    def decMoney(x: Int): Player = this.copy(money = this.money - x)

    def resetJailCount: Player = this.copy(jailCount = 0)

    def moveToStart: Player = this.copy(position = 0)

    def moveToJail: Player = this.copy(position = 10, jailCount = 0)

    def setRollForPosition(x: Int): Player = this.copy(rollForPosition = x)

    def setTurnPosition(x: Int): Player = this.copy(turnPosition = x)

    def setFigure(imgView: ImageView): Player = this.copy(figure = imgView)

}