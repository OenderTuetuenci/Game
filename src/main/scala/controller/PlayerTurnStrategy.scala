package controller

import Game.Monopoly.gameState._
import Game.Monopoly.playerController
import model._
import util.Observable

object PlayerTurnStrategy extends Observable {

    //todo var executePlayerTurn: Unit = { ???????????????????????
    def executePlayerTurn: Unit = {
        if (players(isturn).jailCount > -1) turnInJail() else normalTurn()
    }

    // Todo handeln, strassen verkaufen,hypothek bezahlen etc vor dem wuerfeln
    // Todo und nach dem wuerfeln falls er nicht pleite ist

    def turnInJail(): Unit = {
        notifyObservers(playerInJailEvent(players(isturn)))

        PlayerIsHumanOrNpcStrategy.selectOption // todo zug für spieler oder npc

        //checkHypothek() // schauen ob haeuser im besitz hypotheken haben und bezahlen wenns geht
        val option = "rollDice"
        if (option == "buyOut") {
            players = players.updated(isturn, players(isturn).decMoney(200))
            playerController.checkDept(-1) // owner = bank
            players = players.updated(isturn, players(isturn).resetJailCount)
            notifyObservers(playerIsFreeEvent(players(isturn)))
            PlayerTurnStrategy.executePlayerTurn // zug ausfuehren
        }
        // ...die freikarte benutzen....
        if (option == "useCard") {
            notifyObservers(playerIsFreeEvent(players(isturn)))
            PlayerTurnStrategy.executePlayerTurn // zug ausfuehren
        }
        // ...oder pasch wuerfeln.
        if (option == "rollDice") {
            val throwDices = playerController.wuerfeln
            if (throwDices._3) {
                // bei gewuerfelten pasch kommt man raus und moved
                players = players.updated(isturn, players(isturn).resetJailCount)
                notifyObservers(playerIsFreeEvent(players(isturn)))
                playerController.movePlayer(throwDices._1 + throwDices._2)
            } else {
                //sonst jailcount +1
                players = players.updated(isturn, players(isturn).incJailTime)
                // wenn man 3 runden im jail ist kommt man raus, zahlt und moved
                if (players(isturn).jailCount == 3) {
                    players = players.updated(isturn, players(isturn).resetJailCount)
                    players = players.updated(isturn, players(isturn).decMoney(200))
                    playerController.checkDept(-1) // owner is bank
                    notifyObservers(playerIsFreeEvent(players(isturn)))
                    playerController.movePlayer(throwDices._1 + throwDices._2)
                } else notifyObservers(playerRemainsInJailEvent(players(isturn)))
            }
        }
    }

    def normalTurn(): Unit = {
        notifyObservers(normalTurnEvent(players(isturn)))

        PlayerIsHumanOrNpcStrategy.selectOption // todo zug für spieler oder npc

        //maybe  PlayerIsHumanOrNpcStrategy.selectOption(playerController.checkHypothek)

        playerController.checkHypothek() // schauen ob haeuser im besitz hypotheken haben und bezahlen wenns geht

        // init pasch
        var pasch = true
        var paschCount = 0
        // wuerfeln
        while (pasch) {
            val throwDices = playerController.wuerfeln // 1 = wurf1, 2 = wurf 2, 3 = pasch
            notifyObservers(diceEvent(throwDices._1, throwDices._2, throwDices._3))
            if (throwDices._3) paschCount += 1
            else pasch = false
            //3x pasch gleich jail sonst move player
            if (paschCount == 3) {
                players = players.updated(isturn, players(isturn).moveToJail)
                notifyObservers(playerMoveToJail(players(isturn)))
            } else playerController.movePlayer(throwDices._1 + throwDices._2)
        }
    }
}
