import model.Player
import model.Strasse
import model.Ereignis
import model.Dice
import io.StdIn._

object Game {
  def main(args: Array[String]): Unit = {
    // spielBrett erstellen
    val spielBrett = createSpielBrett()

    // spieleranzahl und namen einlesen
    print("wie viele spieler? : ")
    val playerCount = readInt()
    val players = createPlayers(playerCount)

    // init game
    val dice = Dice()
    var gameOver = false
    var runde = 1

    // start game
    while (!gameOver) {
      println("\nRunde " + runde + " beginnt!\n")
      for (i <- 0 until playerCount) {
        // jeder der noch geld hat darf wuerfeln
        if (players(i).getmoney > 0) {
          // init pasch
          var pasch = true
          var paschCount = 0
          // wuerfeln
          while (pasch) {
            print("\nSpieler: " + players(i).getname()
              + " (pos: " + players(i).getposition
              + " money: " + players(i).getmoney()
              + ") wuerfeln: ")
            val diceThrow1 = dice.throwDice
            val diceThrow2 = dice.throwDice
            val sumDiceThrow = diceThrow1 + diceThrow2
            println("Gewuerfelt: " + sumDiceThrow)
            // schauen ob pasch gewuerfelt wurde
            if (dice.checkPash(diceThrow1, diceThrow2)) {
              println("Pasch")
              paschCount += 1
            } else {
              pasch = false
            }
            //3x pasch gleich jail
            if (paschCount == 3) {
              println("3x Pash -> jail")
              players(i) = players(i).moveToJail()
            }
            //todo if player in jail else move player
            if (false) {
              // player is in jail
              print("todo jail")
            } else {
              // move player
              players(i) = players(i).move(sumDiceThrow)
              val betretenesFeld = spielBrett(players(i).getposition())
              // optionen für das feld holen
              val option = betretenesFeld.onPlayerEntered() //todo type.onPlayerEntered
              println("option: " + option)
              // option ausführen
              if (option == "buy") {
                // wer geld hat kauft die straße
                if (players(i).getmoney() >= spielBrett(i).getPreis) {
                  println("buy street")
                  players(i) = players(i).decMoney(betretenesFeld.getPreis)
                  spielBrett(players(i).getposition()) = betretenesFeld.setOwner(players(i).getname())
                  println(players(i).getmoney())
                } else {
                  println("Can´t afford street")
                }
              //ansonsten miete zahlen
              } else if (option == "pay") {
                // mietpreis holen
                val rent = betretenesFeld.getMiete
                println("pay rent: " + rent)

                // miete beim besitzer hinzufügen
                for (j <- 0 until playerCount) {
                  if (players(j).getname == betretenesFeld.getOwner) {
                    if (players(j).getmoney() > 0) {
                      // betrag zahlen
                      players(i) = players(i).decMoney(rent)
                      players(j) = players(j).incMoney(rent)
                      // wenn bankrott straßen freigeben
                      if (players(i).getmoney() <= 0) {
                        for (k <- 0 to 9) {
                          if (spielBrett(k).getOwner == players(i).getname()) spielBrett(k) = spielBrett(k).setOwner("")
                        }
                        // print player ausgeschieden
                        println(players(i).getname() + " ist pleite.")
                        //Thread.sleep(3000)

                      }
                    }
                  }
                }
              }
            }
            // zugende
            //Thread.sleep(1000) // wait for 1000 millisecond between player moves
            println("new pos: " + players(i).getposition)
          }
        }
      }
      // Rundenende
      println("\n\nRunde " + runde + " vorbei:")
      runde += 1
      println("\nSpieler: ")
      for (player <- players) println(player.toString)
      println("\nStraßen: ")
      for (strasse <- spielBrett) println(strasse.toString)

      Thread.sleep(1000) // wait for 1000 millisecond between rounds

      // Spielende abfragen
      var playersWithMoney = playerCount
      for (i <- 0 until playerCount) {
        if (players(i).getmoney <= 0)
          playersWithMoney -= 1
      }
      if (playersWithMoney <= 1)
        gameOver = true
    }
    // print winner
    print("Spielende: ")
    for (player <- players)
      if (player.getmoney > 0)
        print(player.getname() + " is the winner!")
  }

  def createSpielBrett(): Array[Strasse] = {
    // todo array of any unterklasse of feld ??
    val feld = Array.ofDim[Strasse](10)

    //    spielBrett(0) = Strasse("Strasse1",0,100,"",100)
    //    spielBrett(1) = Ereignis("Ereignis1",0)
    //    spielBrett(2) = Strasse("Strasse3",0,100,"",100)
    //    spielBrett(3) = Ereignis("Ereignis2",0)
    //    spielBrett(4) = Strasse("Strasse5",0,100,"",100)
    //    spielBrett(5) = Ereignis("Ereignis3",0)
    //    spielBrett(6) = Strasse("Strasse7",0,100,"",100)
    //    spielBrett(7) = Ereignis("Ereignis4",0)
    //    spielBrett(8) = Strasse("Strasse9",0,100,"",100)
    //    spielBrett(9) = Ereignis("Ereignis5",0)

    feld(0) = Strasse("Strasse1", 0, 100, "", 100)
    feld(1) = Strasse("Strasse2", 0, 200, "", 200)
    feld(2) = Strasse("Strasse3", 0, 500, "", 500)
    feld(3) = Strasse("Strasse4", 0, 750, "", 750)
    feld(4) = Strasse("Strasse5", 0, 1000, "", 1000)
    feld(5) = Strasse("Strasse6", 0, 1500, "", 1500)
    feld(6) = Strasse("Strasse7", 0, 2000, "", 2000)
    feld(7) = Strasse("Strasse8", 0, 2500, "", 2500)
    feld(8) = Strasse("Strasse9", 0, 3000, "", 3000)
    feld(9) = Strasse("Strasse10", 0, 4000, "", 4000)

    feld
  }

  def createPlayers(n: Int): Array[Player] = {
    val feld = Array.ofDim[Player](n)
    // spieler mit namen einlesen
    for (i <- 0 until n) {
      println("Enter name player" + (i + 1) + ":")
      feld(i) = Player(readLine())
    }
    feld
  }
}