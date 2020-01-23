package model

trait Event

case class NewGameStartedTui() extends Event

case class GameSavedEventTui() extends Event

case class GameLoadedEventTui() extends Event

case class AuctionStartedEventTui(field: Buyable) extends Event

case class AuctionEndedEventTui(player: PlayerInterface, field: Buyable) extends Event

case class displayRollForPositionsEventTui() extends Event

case class brokeEventTui(player: PlayerInterface) extends Event

case class gameFinishedEventTui(winner: PlayerInterface) extends Event

case class payRentEventTui(from: PlayerInterface, to: PlayerInterface) extends Event

case class buyStreetEventTui(player: PlayerInterface, street: Buyable) extends Event

case class buyHouseEventTui(player: PlayerInterface, street: Buyable) extends Event

case class playerInJailEventTui(player: PlayerInterface) extends Event

case class playerRemainsInJailEventTui(player: PlayerInterface) extends Event

case class normalTurnEventTui(player: PlayerInterface) extends Event

case class diceEventTui(player: PlayerInterface, eyeCount1: Int, eyeCount2: Int, pasch: Boolean) extends Event

case class playerSellsStreetEventTui(from: PlayerInterface, street: Street) extends Event

case class playerUsesHyptohekOnStreetEventTui(player: PlayerInterface, street: Street) extends Event

case class playerPaysHyptohekOnStreetEventTui(player: PlayerInterface, street: Street) extends Event

case class playerHasDeptEventTui(player: PlayerInterface, ownerIdx: Int) extends Event

case class streetOnHypothekEventTui(street: Street) extends Event

case class newRoundEventTui(round: Int) extends Event

case class playerMoveToJailTui(player: PlayerInterface) extends Event

case class playerMoveEventTui(player: PlayerInterface, field: Cell) extends Event

case class playerIsFreeEventTui(player: PlayerInterface) extends Event

case class playerRemainsInJailTui(player: PlayerInterface) extends Event

case class playerWentOverGoEventTui(player: PlayerInterface) extends Event

case class playerWentOnGoEventTui(player: PlayerInterface) extends Event

// GUI

case class UpdateGuiDiceLabelEvent(roll1: Int, roll2: Int, pasch: Boolean) extends Event

case class ClearGuiElementsEvent() extends Event

case class MovePlayerFigureEvent(x: Double, y: Double) extends Event

case class OpenMainWindowEvent() extends Event

case class openGoToJailPaschDialog() extends Event

case class openGameOverDialogEvent() extends Event

case class OpenGameWindowEvent() extends Event

case class OpenGetPlayersDialogEvent() extends Event

case class OpenGetNameDialogEvent(currPlayer: Int) extends Event

case class OpenRollForPosDialogEvent(player: PlayerInterface) extends Event

case class OpenInformationDialogEvent() extends Event

case class OpenConfirmationDialogEvent() extends Event

case class OpenInJailDialogEvent() extends Event

case class OpenNormalTurnDialogEvent(player: PlayerInterface) extends Event

case class OpenPlayerFreeDialog(player: PlayerInterface) extends Event

case class UpdateListViewPlayersEvent() extends Event

case class UpdateListViewEventLogEvent(str: String) extends Event

case class OpenPlayerDeptDialog(ownerIdx: Int) extends Event

case class OpenAuctionDialogEvent(field: Buyable) extends Event

// Field Dialogs

case class OpenPayRentDialog(field: Buyable) extends Event

case class OpenBuyableFieldDialog(field: Buyable) extends Event

case class OpenPlayerEnteredGoDialog(field: Los) extends Event

case class OpenPlayerPassedGoDialog() extends Event

case class OpenIncomeTaxDialog(field: IncomeTax) extends Event

case class openGoToJailDialog(field: GoToJail) extends Event

case class OpenVisitJailDialog(field: Jail) extends Event

case class OpenParkFreeDialog(field: FreiParken) extends Event

case class OpenLuxuaryTaxDialog(field: Zusatzsteuer) extends Event

case class OpenChanceDialog() extends Event

case class OpenCommunityChestDialog() extends Event
