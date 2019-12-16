package model

import javafx.scene.image.ImageView

trait PrintEvent

case class askBuyHomeEvent() extends PrintEvent

case class askUndoGetPlayersEvent() extends PrintEvent

case class answerEvent() extends PrintEvent

case class askBuyEvent() extends PrintEvent

case class newGameEvent() extends PrintEvent

case class gameIsGoingToStartEvent() extends PrintEvent

case class displayRollForPositionsEvent() extends PrintEvent

case class brokeEvent(player: Player) extends PrintEvent

case class gameFinishedEvent(winner: Player) extends PrintEvent

case class payRentEvent(from: Player, to: Player) extends PrintEvent

case class buyStreetEvent(player: Player, street: Buyable) extends PrintEvent

case class buyTrainstationEvent(player: Player, street: Street) extends PrintEvent

case class playerInJailEvent(player: Player) extends PrintEvent

case class playerRemainsInJailEvent(player: Player) extends PrintEvent

case class normalTurnEvent(player: Player) extends PrintEvent

case class diceEvent(eyeCount1: Int, eyeCount2: Int, pasch: Boolean) extends PrintEvent

case class playerSellsStreetEvent(from: Player, street: Street) extends PrintEvent

case class playerUsesHyptohekOnStreetEvent(player: Player, street: Street) extends PrintEvent

case class playerPaysHyptohekOnStreetEvent(player: Player, street: Street) extends PrintEvent

case class playerHasDeptEvent(player: Player) extends PrintEvent

case class streetOnHypothekEvent(street: Street) extends PrintEvent

case class newRoundEvent(round: Int) extends PrintEvent

case class PlacePlayersOnBoardEvent() extends PrintEvent

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

// GUI

case class UpdateGuiDiceLabelEvent(roll1: Int, roll2: Int, pasch: Boolean) extends PrintEvent

case class ClearGuiElementsEvent() extends PrintEvent

case class MovePlayerFigureEvent(playerFigure: ImageView, x: Double, y: Double) extends PrintEvent
case class OpenMainWindowEvent() extends PrintEvent

case class openGoToJailDialog() extends PrintEvent

case class openGameOverDialogEvent() extends PrintEvent

case class OpenGameWindowEvent() extends PrintEvent

case class OpenGetPlayersDialogEvent() extends PrintEvent

case class OpenGetNameDialogEvent(currPlayer: Int) extends PrintEvent

case class OpenRollForPosDialogEvent(player: Player) extends PrintEvent

case class OpenRollDiceDialogEvent(player: Player) extends PrintEvent

case class OpenInformationDialogEvent() extends PrintEvent

case class OpenConfirmationDialogEvent() extends PrintEvent

case class OpenInJailDialogEvent() extends PrintEvent

case class OpenPlayerFreeDialog() extends PrintEvent

case class OpenBuyableFieldDialog() extends PrintEvent

case class UpdateListViewPlayersEvent() extends PrintEvent
