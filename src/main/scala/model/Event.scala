package model

abstract class Event

case class brokeEvent(player: Player) extends Event
case class gameOverEvent(winner: Player) extends Event

case class payRentEvent(from: Player, to: Player) extends Event

case class buyStreetEvent(player: Player, street: Street) extends Event

case class buyTrainstationEvent(player: Player, street: Street) extends Event
case class playerInJailEvent(player: Player) extends Event

case class playerRemainsInJailEvent(player: Player) extends Event

case class normalTurnEvent(player: Player) extends Event

case class diceEvent(eyeCount1: Int, eyeCount2: Int, pasch: Boolean) extends Event

case class playerSellsStreetEvent(from: Player, street: Street) extends Event

case class playerUsesHyptohekOnStreetEvent(player: Player, street: Street) extends Event

case class playerPaysHyptohekOnStreetEvent(player: Player, street: Street) extends Event

case class playerSellsTrainstationEvent(from: Player, street: Trainstation) extends Event

case class playerUsesHyptohekOnTrainstationEvent(player: Player, street: Trainstation) extends Event

case class playerPaysHyptohekOnTrainstationEvent(player: Player, street: Trainstation) extends Event

case class playerHasDeptEvent(player: Player) extends Event
case class streetOnHypothekEvent(street: Street) extends Event

case class newRoundEvent(round: Int) extends Event

case class endRoundEvent(round: Int) extends Event
case class playerMoveToJail(player: Player) extends Event

case class optionEvent(option: String) extends Event
case class printEverythingEvent() extends Event
case class playerMoveEvent(player: Player) extends Event
case class playerIsFreeEvent(player: Player) extends Event
case class playerRemainsInJail(player: Player) extends Event
case class playerWentOverGoEvent(player: Player) extends Event
case class playerWentOnGoEvent(player: Player) extends Event
