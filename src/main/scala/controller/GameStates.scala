package controller
import Game.Game._
import model._
import util.Observable

object GameStates extends Observable {

    var gameOver = false
    //var state = beforeGameStarts // todo ????

    def handle(e: GameStateEvent): Unit = { // todo ??????????????????? type(state)
        e match {
            case e: beforeGameStartsEvent => beforeGameStarts
            case e: rollForPositionsEvent => rollForPositionsState
            case e: createPlayersEvent => createPlayersState(e.playerCount, e.playerNames)
            case e: createBoardEvent => createBoardState
            case e: runRoundEvent => runRoundState
            case e: checkGameOverEvent => checkGameOverState
            case e: gameOverEvent => gameOverState
            case _ => println(e)
        }
        //state //todo return state
    }

    def beforeGameStarts = tuiController.notifyObservers(gameIsGoingToStartEvent())

    def createPlayersState(newPlayerCount: Int, playerNames: Array[String]): Unit = {
        for (i <- 0 until newPlayerCount) players = players :+ Player(playerNames(i))
        playerCount = newPlayerCount
    }

    def rollForPositionsState = {
        tuiController.notifyObservers(displayRollForPositionsEvent())

        // jeden einmal wuerfeln lassen
        for (i <- 0 until playerCount) {
            //todo if player is npc or not
            val rollDices = playerController.wuerfeln
            println(players(i).name + " rolled " + (rollDices._1 + rollDices._2))
            //ergebnis speichern für jeden spieler
            players = players.updated(i, players(i).setRollForPosition(rollDices._1 + rollDices._2))
        }
        // nach reihenfolge sortieren
        players = players.sortBy(-_.rollForPosition) // - für reversed
        //reihenfolge erstmalig festlegen
        println("spieler mit reihenfolge:")
        for (i <- 0 until playerCount) {
            players = players.updated(i, players(i).setTurnPosition(i))
            println(players(i))
        }
        // Spieler suchen die das gleiche gewuerfelt haben
        var rolledSame: Vector[Player] = Vector[Player]()
        for (i <- 0 until playerCount) {
            for (j <- (i + 1) until playerCount) {
                if (players(i).rollForPosition == players(j).rollForPosition) {
                    rolledSame = rolledSame :+ players(i)
                    rolledSame = rolledSame :+ players(j)
                }
            }
        }


        println("leute die nochmal würfeln dürfen ")
        for (player <- rolledSame) println(player)
        //todo lass alle die jeweils das gleiche gewuerfelt haben
        // so lange nochmal würfeln aber nur ihre position untereinander tauschen
        // bis keiner mehr das gleiche würfelt

        // entgueltige reihenfolge festlegen
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
        tuiController.notifyObservers(newRoundEvent(round))
        for (i <- 0 until playerCount) {
            isturn = i
            PlayerTurnStrategy.executePlayerTurn // zug ausfuehren
            //Thread.sleep(1000) // wait for 1000 millisecond between turns
        }
        // Rundenende
        tuiController.notifyObservers(endRoundEvent(round))
        tuiController.notifyObservers(printEverythingEvent())
        round += 1
        //Thread.sleep(1000) // wait for 1000 millisecond between rounds
    }


    def checkGameOverState = {
        if (playerCount == 1) gameOver = true
    }

    def gameOverState = {
        // am ende ist nurnoch 1 spieler uebrig
        tuiController.notifyObservers(gameFinishedEvent(players(0)))
    }
}