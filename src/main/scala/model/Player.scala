package model

case class Player(name: String, position:Int = 0,money:Int = 20000){
  override def toString: String = "name: " + name + " pos: " + position +" money: "+money
  //Moves player about x places
  def move(x:Int) : Player = {
    var pos = this.position + x
    while (pos >= 10) pos -= 10
    Player(name,pos,money)
  }
  def setname(x:String) : Player = Player(x,position,money)
  def incMoney(x:Int) : Player = Player(name,position,this.money + x)
  def decMoney(x:Int) : Player = Player(name,position,this.money - x)

  //Moves Player x places over start
  def moveToStart : Player = Player(name,0,money)
  def moveToJail : Player = Player(name,5,money)
}
