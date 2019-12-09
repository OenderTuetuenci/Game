package model

trait GameStateEvent

case class getPlayersEvent(playerCount: Int, npcCount: Int) extends GameStateEvent

case class rollForPositionsEvent() extends GameStateEvent

case class createBoardAndPlayersEvent(playerNames: Vector[String], npcNames: Vector[String]) extends GameStateEvent

case class runRoundEvent() extends GameStateEvent
case class checkGameOverEvent() extends GameStateEvent
case class gameOverEvent() extends GameStateEvent

