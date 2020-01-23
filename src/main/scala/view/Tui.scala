package view

import controller.controllerComponent.ControllerInterface
import model._
import util.Observer

import scala.language.implicitConversions

class Tui(controller: ControllerInterface) extends Observer {
    controller.add(this)

    def getController: ControllerInterface = controller

    override def update(e: Event): Any = {
        var string = ""
        e match {
            //Output
            case e: NewGameStartedTui => string = getNewGameStartedString(e)
            case e: GameSavedEventTui => string = getGameWasSavedString(e)
            case e: AuctionStartedEventTui => string = getAuctionStartedString(e)
            case e: AuctionEndedEventTui => string = getAuctionEndedString(e)
            case e: GameLoadedEventTui => string = getGameWasLoadedString(e)
            case e: displayRollForPositionsEventTui => string = getRollForPositionsString(e)
            case e: brokeEventTui => string = getBrokeEventString(e)
            case e: gameFinishedEventTui => string = getFinishedString(e)
            case e: payRentEventTui => string = getPayRentString(e)
            case e: buyStreetEventTui => string = getBuyStreetEventString(e)
            case e: playerInJailEventTui => string = getPlayerInJailString(e)
            case e: normalTurnEventTui => string = getNormalTurnString(e)
            case e: diceEventTui => string = getRollString(e)
            case e: playerSellsStreetEventTui => string = getPlayerSellsStreetString(e)
            case e: playerUsesHyptohekOnStreetEventTui => string = getPlayerUsesHypothekOnStreetString(e)
            case e: playerPaysHyptohekOnStreetEventTui => string = getPlayerPaysHypothekOnStreetString(e)
            case e: newRoundEventTui => string = getNewRoundString(e)
            case e: playerMoveToJailTui => string = getPlayerMoveToJailString(e)
            case e: playerMoveEventTui => string = getPlayerMovedString(e)
            case e: playerIsFreeEventTui => string = getPlayerIsFreeString(e)
            case e: playerRemainsInJailEventTui => string = getPlayerRemainsInJailString(e)
            case e: playerWentOverGoEventTui => string = getPlayerWentOverGoString(e)
            case e: playerWentOnGoEventTui => string = getPlayerWentOnGoString(e)
            case e: streetOnHypothekEventTui => string = getStreetOnHypothekString(e)
            case e: playerHasDeptEventTui => string = getPlayerHasDeptEventString(e)
            case _ =>
        }
        if (string != "") controller.notifyObservers(UpdateListViewEventLogEvent(string))
    }

    def getNewGameStartedString(e: NewGameStartedTui): String = "A new game started."

    def getGameWasSavedString(e: GameSavedEventTui): String = "A game was saved."

    def getGameWasLoadedString(e: GameLoadedEventTui): String = "A game was loaded."

    def getAuctionStartedString(e: AuctionStartedEventTui): String = "Auction for " + e.field.name + " started."

    def getAuctionEndedString(e: AuctionEndedEventTui): String = e.player.name + " won the auction for " + e.field.name

    def getRollForPositionsString(e: displayRollForPositionsEventTui): String = "Players roll for positions."

    def getRollString(e: diceEventTui): String = { //SS
        var string = ""
        string += e.player.name + " rolled "
        if (e.pasch)
            string += " pasch"
        string += ": " + (e.eyeCount1 + e.eyeCount2)
        string
    }

    def getNormalTurnString(e: normalTurnEventTui): String = {
        val string = "Its " + e.player.name + "´s turn!"
        string
    }

    def getPlayerInJailString(e: playerInJailEventTui): String = {
        var string = "Its " + e.player.name + "´s turn. he is in jail!\n"
        string += "Jailcount: " + (e.player.jailCount + 1)
        string
    }

    def getPlayerIsFreeString(e: playerIsFreeEventTui): String = e.player.name + " is free again!"

    def getPlayerRemainsInJailString(e: playerRemainsInJailEventTui): String = e.player.name + " remains in jail"

    def getStreetOnHypothekString(e: streetOnHypothekEventTui): String = e.street.name + " is on hypothek."

    def getBuyStreetEventString(e: buyStreetEventTui): String = {
        e.player.name + " bought " + e.street.name
    }

    def getPayRentString(e: payRentEventTui): String = e.from.name + " pays rent to " + e.to.name

    def getPlayerWentOverGoString(e: playerWentOverGoEventTui): String = e.player.name + " went over go."

    def getPlayerWentOnGoString(e: playerWentOnGoEventTui): String = e.player.name + " went on go and gets extra money."

    def getFinishedString(e: gameFinishedEventTui): String = e.winner.name + " is the winner!!"

    def getBrokeEventString(e: brokeEventTui): String = e.player.name + " is broke!!"

    def getPlayerUsesHypothekOnStreetString(e: playerUsesHyptohekOnStreetEventTui): String = {
        e.player.name + " gets Hypothek for " + e.street.name + " new creditbalance: " + e.player.money

    }

    def getPlayerPaysHypothekOnStreetString(e: playerPaysHyptohekOnStreetEventTui): String = {
        e.player.name + " pays Hypothek for " + e.street.name + " new creditbalance: " + e.player.money
    }

    def getPlayerSellsStreetString(e: playerSellsStreetEventTui): String = {
        var string = e.from.name + " sells " + e.street.name + "\n"
        string += "new creditbalance: " + e.from.money
        string
    }

    def getNewRoundString(e: newRoundEventTui): String = "round " + e.round + " starts"

    def getPlayerMoveToJailString(e: playerMoveToJailTui): String = e.player.name + " moved to Jail!"

    def getPlayerMovedString(e: playerMoveEventTui): String = e.player.name + " moved to " + e.field.name

    def getPlayerHasDeptEventString(e: playerHasDeptEventTui): String = {
        var string = e.player.name + " has " + e.player.money + " debts at "
        if (e.ownerIdx == -1) string += "the bank"
        else string += controller.players(e.ownerIdx).name
        string
    }

    def getbuyHouseEventTui(e: buyHouseEventTui): String = {
        e.player.money + " bought a house for " + e.street.name
    }

}