package model

case class Player(name: String, position: Int = 0, money: Int = 10000, jailCount: Int = -1,ownedStreet:Vector[Int]=Vector[Int]()) {
    override def toString: String = "name: " + this.name + " pos: " + this.position + " money: " + this.money + " roundsInJail: " + this.jailCount
    //Moves player about x places
    def move(x: Int): Player = Player(name, this.position + x, money, jailCount,ownedStreet)
    def buyStreet(streetNr:Int): Player = Player(name,position,money,jailCount,this.ownedStreet :+streetNr)

    def sellStreet(streetNr:Int): Player = Player(name,position,money,jailCount,this.ownedStreet.filterNot(o => o == streetNr))
    //Moves player about x places
    def moveBack(x: Int): Player = Player(name, this.position - x, money, jailCount,ownedStreet)

    def incJailTime: Player = Player(name, position, money, this.jailCount + 1,ownedStreet)

    def incMoney(x: Int): Player = Player(name, position, this.money + x, jailCount,ownedStreet)

    def decMoney(x: Int): Player = Player(name, position, this.money - x, jailCount,ownedStreet)

    def resetJailCount: Player = Player(name, position, money, -1,ownedStreet)

    //Moves Player x places over start
    def moveToStart: Player = Player(name, 0, money, jailCount,ownedStreet)

    def moveToJail: Player = Player(name, 10, money, 0,ownedStreet)
}