package model

case class Player(name: String,position:Int = 0){
  override def toString: String = name
}
