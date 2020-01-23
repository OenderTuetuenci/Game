package model

trait GameStateEvent

case class InitGameEvent() extends GameStateEvent

case class getPlayersEvent() extends GameStateEvent

case class rollForPositionsEvent() extends GameStateEvent

case class StartFirstRoundEvent() extends GameStateEvent

case class createBoardAndPlayersEvent() extends GameStateEvent

case class runRoundEvent() extends GameStateEvent

case class checkGameOverEvent() extends GameStateEvent

case class gameOverEvent() extends GameStateEvent

