package controller

import Game.Monopoly.playerController
import model._
import util.Observable

class GameStates extends Observable {
    val dice = Dice()
    var humanPlayers = 0
    var npcPlayers = 0
    var playerCount = 0
    var playerNames: Array[String] = Array[String]()
    var gameOver = false
    var npcNames: Array[String] = Array[String]()
    var board: Vector[Cell] = Vector[Cell]()
    var players: Vector[Player] = Vector[Player]()
    var isturn = 0 // aktueller spieler
    var round = 1
    //var state = beforeGameStarts // todo ????

    def handle(e: GameStateEvent): Unit = { // todo ??????????????????? type(state)
        e match {
            case e: beforeGameStartsEvent => beforeGameStarts(e)
            case e: rollForPositionsEvent => rollForPositionsState
            case e: createPlayersEvent => createPlayersState
            case e: createBoardEvent => createBoardState
            case e: runRoundEvent => runRoundState
            case e: checkGameOverEvent => checkGameOverState
            case e: gameOverEvent => gameOverState
            case _ => println(e)
        }
        //state //todo return state
    }

    def beforeGameStarts() = notifyObservers(gameIsGoingToStartEvent())

    // todo settings oder so vlt noch wie viele spieler ...npc mit rein


    def createPlayersState: Unit = {
        for (i <- 0 until humanPlayers) players = players :+ Player(playerNames(i))
        for (i <- 0 until npcPlayers) players = players :+ Player(npcNames(i), isNpc = true)
        playerCount = humanPlayers + npcPlayers
    }

    def rollForPositionsState = {
        notifyObservers(displayRollForPositionsEvent())

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

        var playersRollingAgain: Vector[Player] = Vector[Player]()
        var playersRollingAgainPositions: Vector[Int] = Vector[Int]()
        //while (!(rolledSame.isempty)) { // solange es spieler gibt die das gleiche gewuerfelt haben
        for (i <- 2 until 12) {
            //jede augenzahl durchgehen
            for (player <- players) {
                // spieler die diese augenzahl gewürfelt haben raussuchen
                // auch die positionen raussuchen
                // z.b. a und b haben beide 9 und wuerfeln nochmal für pos 1 und 2
                if (player.rollForPosition == i) {
                    playersRollingAgain :+ player
                    playersRollingAgainPositions :+ player.turnPosition
                }
            }
            // spieler nochmal so lange wuerfeln lassen bis sie nichtmehr das gleiche haben
            // do
            for (player <- playersRollingAgain) {
                //todo if player is npc or not
                val rollDices = playerController.wuerfeln
                println(player.name + " rolled " + (rollDices._1 + rollDices._2))
                //ergebnis speichern für jeden spieler
                players = players.updated(i, players(i).setRollForPosition(rollDices._1 + rollDices._2))
            }
            // while not das gleiche gewuerfelt
            // position untereinander bestimmten
            //sort players nach augenzahl absteigend
            // positionen sollten aufsteigend sein
            //var i = 0 ( i ist index von spielern sortiert, der mit höchsten ist 1.)
            // for (position <- positions)
            // players(i).setTurnPosition(position) ( und bekommt die 1. position )
            //i += 1
        }

        //} END while


        // entgueltige reihenfolge festlegen
        notifyObservers(printEverythingEvent())
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
        if (playerCount == 1) gameOver = true
    }

    def gameOverState = {
        // am ende ist nurnoch 1 spieler uebrig
        notifyObservers(gameFinishedEvent(players(0)))
    }
}