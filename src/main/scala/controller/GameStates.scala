package controller

import Game.Game._
import model._
import util.Observable

object GameStates extends Observable {
    var gameOver = false
    var state = beforeGameStarts

    def handle(e: GameStateEvent): Any = { // todo ??????????????????? type(state)
        e match {
            case e: beforeGameStartsEvent => state = beforeGameStarts
            case e: createPlayersEvent => state = createPlayersState(e.playerCount, e.playerNames)
            case e: createBoardEvent => state = createBoardState
            case e: runRoundEvent => state = runRoundState
            case e: checkGameOverEvent => state = checkGameOverState
            case e: gameOverEvent => state = gameOverState
        }
        state
    }

    def beforeGameStarts = notifyObservers(gameIsGoingToStartEvent())

    def createPlayersState(newPlayerCount: Int, playerNames: Array[String]): Unit = {
        for (i <- 0 until newPlayerCount) players = players :+ Player(playerNames(i))
        playerCount = playerCount
    }

    def createBoardState = {
        board = board :+ Los("Los")
        board = board :+ Street("Strasse1", 1, 60, -1, 200, 0, mortgage = false)
        board = board :+ CommunityChest("Gemeinschaftsfeld1")
        board = board :+ Street("Strasse2", 1, 60, -1, 200, 0, mortgage = false)
        board = board :+ IncomeTax("Einkommensteuer")
        board = board :+ Trainstation("Suedbahnhof", 9, 200, -1, 200, hypothek = false)
        board = board :+ Street("Strasse3", 2, 100, -1, 200, 0, mortgage = false)
        board = board :+ Eventcell("Ereignisfeld1")
        board = board :+ Street("Strasse4", 2, 100, -1, 200, 0, mortgage = false)
        board = board :+ Street("Strasse5", 2, 120, -1, 200, 0, mortgage = false)
        board = board :+ Jail("Zu besuch oder im Gefaengnis")

        board = board :+ Street("Strasse6", 3, 140, -1, 200, 0, mortgage = false)
        board = board :+ Elektrizitaetswerk("Elektrizitaetswerk", 10, 150, -1, 200, hypothek = false)
        board = board :+ Street("Strasse7", 3, 140, -1, 200, 0, mortgage = false)
        board = board :+ Street("Strasse8", 3, 160, -1, 200, 0, mortgage = false)
        board = board :+ Trainstation("Westbahnhof", 9, 200, -1, 200, hypothek = false)
        board = board :+ Street("Strasse9", 4, 180, -1, 200, 0, mortgage = false)
        board = board :+ CommunityChest("Gemeinschaftsfeld2")
        board = board :+ Street("Strasse10", 4, 180, -1, 200, 0, mortgage = false)
        board = board :+ Street("Strasse11", 4, 200, -1, 200, 0, mortgage = false)
        board = board :+ FreiParken("Freiparken")

        board = board :+ Street("Strasse12", 5, 220, -1, 200, 0, mortgage = false)
        board = board :+ Eventcell("Ereignisfeld2")
        board = board :+ Street("Strasse13", 5, 220, -1, 200, 0, mortgage = false)
        board = board :+ Street("Strasse14", 5, 240, -1, 200, 0, mortgage = false)
        board = board :+ Trainstation("Nordbahnhof", 9, 200, -1, 200, hypothek = false)
        board = board :+ Street("Strasse15", 6, 260, -1, 500, 0, mortgage = false)
        board = board :+ Street("Strasse16", 6, 260, -1, 800, 0, mortgage = false)
        board = board :+ Wasserwerk("Wasserwerk", 10, 150, -1, 200, hypothek = false)
        board = board :+ Street("Strasse17", 6, 280, -1, 2500, 0, mortgage = false)
        board = board :+ GoToJail("Gehe ins Gefaengnis")

        board = board :+ Street("Strasse18", 7, 300, -1, 200, 0, mortgage = false)
        board = board :+ Street("Strasse19", 7, 300, -1, 200, 0, mortgage = false)
        board = board :+ CommunityChest("Gemeinschaftsfeld3")
        board = board :+ Street("Strasse20", 7, 320, -1, 200, 0, mortgage = false)
        board = board :+ Trainstation("Nordbahnhof", 9, 200, -1, 200, hypothek = false)
        board = board :+ Eventcell("Ereignisfeld3")
        board = board :+ Street("Strasse21", 8, 350, -1, 200, 0, mortgage = false)
        board = board :+ Zusatzsteuer("Zusatzsteuer")
        board = board :+ Street("Strasse22", 8, 400, -1, 200, 0, mortgage = false)
    }


    def runRoundState = {
        // todo if player has money raus und einfach so rbüer
        notifyObservers(newRoundEvent(round))
        for (i <- 0 until playerCount) {
            isturn = i
            PlayerTurnStrategy.executePlayerTurn // zug ausfuehren
            //Thread.sleep(1000) // wait for 1000 millisecond between turns
        }
        // Rundenende
        notifyObservers(endRoundEvent(round))
        notifyObservers(printEverythingEvent())
        round += 1
        //Thread.sleep(1000) // wait for 1000 millisecond between rounds
    }


    def checkGameOverState = {
        gameOver = true
        //////////////////////
        if (playerCount == 1) gameOver = true
    }

    def gameOverState = {
        for (player <- players)
            notifyObservers(gameFinishedEvent(player))
        //if (!player.gameOver) notifyObservers(gameFinishedEvent(player))
    }
}