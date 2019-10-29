import model.Player
import model.Street
import model.Eventcell
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
      for (amZug <- 0 until playerCount) {
        // jeder der noch geld hat darf wuerfeln
        if (players(amZug).money > 0) {
          // init pasch
          var pasch = true
          var paschCount = 0
          // wuerfeln
          while (pasch) {
            print("\nSpieler: " + players(amZug).toString + ") wuerfeln: ")
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
              players(amZug) = players(amZug).moveToJail
            }
            //todo if player in jail else move player
            if (false) {
              // player is in jail
              print("todo jail")
            } else {
              // move player
              players(amZug) = players(amZug).move(sumDiceThrow)
              val betretenesFeld = spielBrett(players(amZug).position)
              // optionen für das feld holen
              val option = betretenesFeld.onPlayerEntered(players(amZug)) //todo type.onPlayerEntered
              println("option: " + option)

              // option ausführen
              if (option == "buy") {
                // wer geld hat kauft die straße
                if (players(amZug).money >= spielBrett(players(amZug).position).price) {
                  println("buy street")
                  players(amZug) = players(amZug).decMoney(betretenesFeld.price)
                  spielBrett(players(amZug).position) = betretenesFeld.setOwner(players(amZug).name)
                  println(players(amZug).money)
                } else {
                  println("Can´t afford street")
                }
              //ansonsten miete zahlen
              } else if (option == "pay") {
                // mietpreis holen
                val rent = betretenesFeld.rent
                println("pay rent: " + rent)

                // miete beim besitzer hinzufügen
                for (owner <- 0 until playerCount) {
                  if (players(owner).name == betretenesFeld.owner) {
                    // betrag zahlen
                    players(amZug) = players(amZug).decMoney(rent)
                    players(owner) = players(owner).incMoney(rent)
                    // wenn bankrott straßen an owner übergeben wenn hypotheken und verkaufen nicht geht
                    if (players(amZug).money <= 0) {
                      //todo check hypotheken oder verkaufe straße
                      //todo else straßen abgeben
                      for (k <- 0 until 10) {
                        // straßen freigeben
                        //if (spielBrett(k).getOwner == players(amZug).getname())
                        // spielBrett(k) = spielBrett(k).setOwner("")
                        // strassen an spieler geben wo schulden gemacht wurden
                        if (spielBrett(k).owner == players(amZug).name)
                          spielBrett(k) = spielBrett(k).setOwner(players(owner).name)
                      }
                      // print player ausgeschieden
                      println(players(amZug).name + " ist pleite.")
                      //Thread.sleep(3000)
                    }
                  }
                }
              }
              else if(option == "buy home"){
                players(amZug).decMoney(200)
                spielBrett(players(amZug).position) = spielBrett(players(amZug).position).buyHome(1)
              }
            }
            // zugende
            //Thread.sleep(1000) // wait for 1000 millisecond between player moves
            println("new pos: " + players(amZug).position)
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
        if (players(i).money <= 0)
          playersWithMoney -= 1
      }
      if (playersWithMoney <= 1)
        gameOver = true
    }
    // print winner
    print("Spielende: ")
    for (player <- players)
      if (player.money > 0)
        print(player.name + " is the winner!")
  }

  def createSpielBrett(): Array[Street] = {
    // todo array of any unterklasse of feld ??
    val feld = Array.ofDim[Street](10)

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

    feld(0) = Street("Strasse1", 0, 100, "", 100,0)
    feld(1) = Street("Strasse2", 0, 200, "", 200,0)
    feld(2) = Street("Strasse3", 0, 500, "", 300,0)
    feld(3) = Street("Strasse4", 0, 750, "", 450,0)
    feld(4) = Street("Strasse5", 0, 1000, "", 500,0)
    feld(5) = Street("Strasse6", 0, 1500, "", 800,0)
    feld(6) = Street("Strasse7", 0, 2000, "", 1000,0)
    feld(7) = Street("Strasse8", 0, 2500, "", 2500,0)
    feld(8) = Street("Strasse9", 0, 3000, "", 3000,0)
    feld(9) = Street("Strasse10", 0, 4000, "", 4000,0)

    feld
  }

  def createPlayers(n: Int): Array[Player] = {
    val feld = Array.ofDim[Player](n)
    // spieler mit namen einlesens
    for (i <- 0 until n) {
      println("Enter name player" + (i + 1) + ":")
      feld(i) = Player(readLine())
    }
    feld
  }
}