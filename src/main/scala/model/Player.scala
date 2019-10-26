package model

case class Player(name: String, position:Int = 0,money:Int = 1500){
  override def toString: String = name+" P:"+position+" capital: "+money
  //Moves player about x places
  def move(x:Int) : Player = Player(name,position+x,money)
  //Moves Player x places over start
  def moveOverStart(x:Int) : Player = Player(name,0+x,money)
}
