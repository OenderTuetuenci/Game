package model

case class Player(name: String, position: Int = 0, money: Int = 5000, jailCount: Int = -1) {
  override def toString: String = "name: " + this.name + " pos: " + this.position +" money: "+ this.money + " " + this.jailCount
  //Moves player about x places
  def move(x:Int) : Player = {
    Player(name, this.position + x, money,jailCount)
  }
  //Moves player about x places
  def moveBack(x:Int) : Player = {
    Player(name, this.position - x, money,jailCount)
  }
  def incJailTime: Player = {
        Player(name, position, money, this.jailCount + 1)
  }
  def incMoney(x:Int) : Player = Player(name,position,this.money + x,jailCount)
  def decMoney(x:Int) : Player = Player(name,position,this.money - x,jailCount)

  def resetJailCount: Player = Player(name, position, money, -1)

  //Moves Player x places over start
  def moveToStart : Player = Player(name,0,money,jailCount)

  def moveToJail: Player = {
        Player(name, 30, money, 0)
  }
}
