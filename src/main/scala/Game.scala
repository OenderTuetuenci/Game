import model.{Cell, Dice, Eventcell, Player, Street}

import io.StdIn._

object Game {
  //spielBrett erstellen
  val spielBrett:Array[Cell] = createSpielBrett()
  var players:Array[Player] = createPlayers(0)

  def main(args: Array[String]): Unit = {

    // spieleranzahl und namen einlesen
    print("wie viele spieler? : ")
    val playerCount = readInt()
    players = createPlayers(playerCount)

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
              betretenesFeld match {
                case s:Street => activateStreet(players(amZug).position,amZug)
                //case e:Eventcell => activateEvent()
                case _ =>
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
      for(i <- spielBrett.indices){
        spielBrett(i) match {
          case s:Street if s.owner != -1 => println(s.toString + " Owner: "+players(s.owner).name)
          case e:Eventcell => println(e.toString)
          case _ =>
        }
      }

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
  def activateStreet(streetNR:Int, playerNR: Int) : Unit = {
    //Hilfsvariablen
    val street = spielBrett(streetNR).asInstanceOf[Street]

    val option = spielBrett(streetNR).onPlayerEntered(playerNR)
    println("option: " + option)

    if (option == "buy") {
      // wer geld hat kauft die straße
      if (players(playerNR).money >= street.price) {
        println("buy street")
        players(playerNR) = players(playerNR).decMoney(street.price)
        spielBrett(streetNR) = street.setOwner(playerNR)
        println(players(playerNR).money)
      } else {
        println("Can´t afford street")
      }
      //ansonsten miete zahlen
    } else if (option == "pay") {
      // mietpreis holen
      val rent = street.rent
      println("pay rent: " + rent)
      //miete abziehen
      players(playerNR) = players(playerNR).decMoney(rent)
      players(street.owner) = players(street.owner).incMoney(rent)

      if (players(playerNR).money <= 0) {
        //todo check hypotheken oder verkaufe straße
        //todo else straßen abgebe// todo array of any unterklasse of feld ??n
        //Straßen an Besitzer abgebenx
        for (k <- spielBrett.indices) {
          spielBrett(k) match {
            case s: Street => spielBrett(k) = spielBrett(k).asInstanceOf[Street].setOwner(street.owner)
            //case e:Eventcell =>
            case _ =>
          }
        }
        println(players(playerNR).name + " ist pleite")
      }
    }else if(option == "buy home"){
      if(players(playerNR).money > 200)
        players(playerNR).decMoney(200)
      spielBrett(streetNR) = spielBrett(streetNR).asInstanceOf[Street].buyHome(1)
    }
  }


  def createSpielBrett(): Array[Cell] = {
    val spielBrett = new Array[Cell](11)

    spielBrett(0) = Street("Strasse1", 0, 100, -1, 100,0)
    spielBrett(1) = Street("Strasse2", 0, 200, -1, 200,0)
    spielBrett(2) = Street("Strasse3", 0, 500, -1, 300,0)
    spielBrett(3) = Street("Strasse4", 0, 750, -1, 450,0)
    spielBrett(4) = Street("Strasse5", 0, 1000, -1, 500,0)
    spielBrett(5) = Street("Strasse6", 0, 1500, -1, 800,0)
    spielBrett(6) = Street("Strasse7", 0, 2000, -1, 1000,0)
    spielBrett(7) = Street("Strasse8", 0, 2500, -1, 2500,0)
    spielBrett(8) = Street("Strasse9", 0, 3000, -1, 3000,0)
    spielBrett(9) = Street("Strasse10", 0, 4000, -1, 4000,0)
    spielBrett(10) = Eventcell("Ereignis1",0)

    spielBrett
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