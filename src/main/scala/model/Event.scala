package model

abstract class Event
case class brokeEvent(player:Player) extends Event
case class GameOverEvent(winner:Player) extends Event
case class payRentEvent(from:Player,to:Player) extends Event
case class buyEvent(player:Player,street:Cell) extends Event
case class playerInJailEvent(player: Player) extends Event
case class normalTurnEvent() extends Event
case class diceEvent(eyeCount:Int,pasch:Boolean) extends Event