package controller

import Game.Monopoly.gameState._

//todo maybe turnevents for roll dice, buy,sell,

object PlayerIsHumanOrNpcStrategy {

    //var doTurn: Unit = { ???????????????????????

    def selectOption: Unit = {
        if (players(isturn).isNpc) doNpcTurn else doHumanTurn
    }

    def doHumanTurn = println("Player is Human")

    // rollDiceEvent
    //npc wuerfeln
    // buy street,house.... event

    def doNpcTurn = println("Player is Npc")

    // notifyobservers(playerRollDiceEvent)
    // tui.getInput(playerRollDiceEvent)
    // optionen anzeigen
    // optionen auswaehlen

    // rollDiceEvent z.b. auswaehlen

    // buy street,house.... event
}