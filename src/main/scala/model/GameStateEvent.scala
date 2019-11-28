package model
trait GameStateEvent

case class beforeGameStartsEvent() extends GameStateEvent

case class createPlayersEvent(humanPlayers: Int, npcPlayers: Int, playerNames: Array[String], npcNames: Array[String]) extends GameStateEvent {
    // todo settings oder so vlt noch wie viele spieler ...npc mit rein
}
case class createBoardEvent() extends GameStateEvent

case class rollForPositionsEvent() extends GameStateEvent
case class runRoundEvent() extends GameStateEvent
case class checkGameOverEvent() extends GameStateEvent
case class gameOverEvent() extends GameStateEvent

