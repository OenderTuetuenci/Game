package model

trait PrintEvent

case class gameIsGoingToStartEvent() extends PrintEvent

case class brokeEvent(player: Player) extends PrintEvent

case class gameOverEvent(winner: Player) extends PrintEvent

case class payRentEvent(from: Player, to: Player) extends PrintEvent

case class buyStreetEvent(player: Player, street: Street) extends PrintEvent

case class buyTrainstationEvent(player: Player, street: Street) extends PrintEvent

case class playerInJailEvent(player: Player) extends PrintEvent

case class playerRemainsInJailEvent(player: Player) extends PrintEvent

case class normalTurnEvent(player: Player) extends PrintEvent

case class diceEvent(eyeCount1: Int, eyeCount2: Int, pasch: Boolean) extends PrintEvent

case class playerSellsStreetEvent(from: Player, street: Street) extends PrintEvent

case class playerUsesHyptohekOnStreetEvent(player: Player, street: Street) extends PrintEvent

case class playerPaysHyptohekOnStreetEvent(player: Player, street: Street) extends PrintEvent

case class playerSellsTrainstationEvent(from: Player, street: Trainstation) extends PrintEvent

case class playerUsesHyptohekOnTrainstationEvent(player: Player, street: Trainstation) extends PrintEvent

case class playerPaysHyptohekOnTrainstationEvent(player: Player, street: Trainstation) extends PrintEvent

case class playerHasDeptEvent(player: Player) extends PrintEvent

case class streetOnHypothekEvent(street: Street) extends PrintEvent

case class newRoundEvent(round: Int) extends PrintEvent

case class endRoundEvent(round: Int) extends PrintEvent

case class playerMoveToJail(player: Player) extends PrintEvent

case class optionEvent(option: String) extends PrintEvent

case class printEverythingEvent() extends PrintEvent

case class playerMoveEvent(player: Player) extends PrintEvent

case class playerIsFreeEvent(player: Player) extends PrintEvent

case class playerRemainsInJail(player: Player) extends PrintEvent

case class playerWentOverGoEvent(player: Player) extends PrintEvent

case class playerWentOnGoEvent(player: Player) extends PrintEvent

case class falseTestEvent() extends PrintEvent