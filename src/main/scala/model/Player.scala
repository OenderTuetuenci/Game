package model

case class Player(name: String, position:Int = 0,money:Int = 1500,jailCount: Int = 0){
  override def toString: String = "name: " + this.name + " pos: " + this.position +" money: "+ this.money + " " + this.jailCount
  //Moves player about x places
  def move(x:Int) : Player = {
    Player(name, this.position + x, money,jailCount)
  }
  //Moves player about x places
  def moveBack(x:Int) : Player = {
    Player(name, this.position - x, money,jailCount)
  }

  def incJailTime(x:Int) : Player = {
    var count = this.jailCount
    if (count == 3) count = 0
    else count += 1
    Player(name, position, money, count)
  }
  def incMoney(x:Int) : Player = Player(name,position,this.money + x,jailCount)
  def decMoney(x:Int) : Player = Player(name,position,this.money - x,jailCount)

  //Moves Player x places over start
  def moveToStart : Player = Player(name,0,money,jailCount)
  def moveToJail : Player = Player(name,5,money,jailCount)
}
