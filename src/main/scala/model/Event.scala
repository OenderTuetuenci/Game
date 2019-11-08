package model

abstract class Event
case class brokeEvent(player:Player) extends Event
case class GameOverEvent(winner:Player) extends Event
case class payRentEvent(from:Player,to:Player) extends Event
case class buyStreetEvent(player:Player,street:Street) extends Event
case class playerInJailEvent(player: Player,option:String,diceEvent:diceEvent) extends Event
case class normalTurnEvent(player:Player) extends Event
case class diceEvent(eyeCount1:Int,eyeCount2:Int,pasch:Boolean) extends Event
case class playerSellsStreetEvent(from:Player,to:Player,street: Street)
case class newRoundEvent(round:Int)
case class endRoundEvent(round:Int)