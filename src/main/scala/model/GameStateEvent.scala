package model
trait GameStateEvent

case class beforeGameStartsEvent() extends GameStateEvent

case class createPlayersEvent() extends GameStateEvent
case class createBoardEvent() extends GameStateEvent

case class rollForPositionsEvent() extends GameStateEvent
case class runRoundEvent() extends GameStateEvent
case class checkGameOverEvent() extends GameStateEvent
case class gameOverEvent() extends GameStateEvent

