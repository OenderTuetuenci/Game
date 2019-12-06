package model

trait GameStateEvent

case class getPlayersEvent() extends GameStateEvent
case class rollForPositionsEvent() extends GameStateEvent
case class createBoardAndPlayersEvent() extends GameStateEvent
case class runRoundEvent() extends GameStateEvent
case class checkGameOverEvent() extends GameStateEvent
case class gameOverEvent() extends GameStateEvent

