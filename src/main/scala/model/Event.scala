package model

abstract class Event
case class brokeEvent(player:Player) extends Event
case class GameOverEvent(winner:Player) extends Event
case class payRentEvent(from:Player,to:Player) extends Event
case class buyEvent(player:Player,street:Cell) extends Event
case class playerInJailEvent(player: Player,option:String,diceEvent:diceEvent) extends Event
case class normalTurnEvent(player:Player) extends Event
case class diceEvent(eyeCount1:Int,eyeCount2:Int,pasch:Boolean) extends Event