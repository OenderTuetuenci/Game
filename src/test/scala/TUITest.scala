import controller.Controller
import model.{Player, Street, Trainstation, brokeEvent, buyStreetEvent, buyTrainstationEvent, diceEvent, endRoundEvent, falseTestEvent, gameOverEvent, newRoundEvent, normalTurnEvent, optionEvent, payRentEvent, playerHasDeptEvent, playerInJailEvent, playerIsFreeEvent, playerMoveEvent, playerMoveToJail, playerPaysHyptohekOnStreetEvent, playerPaysHyptohekOnTrainstationEvent, playerRemainsInJailEvent, playerSellsStreetEvent, playerSellsTrainstationEvent, playerUsesHyptohekOnStreetEvent, playerUsesHyptohekOnTrainstationEvent, playerWentOnGoEvent, playerWentOverGoEvent, printEverythingEvent, streetOnHypothekEvent}
import org.scalatest.{Matchers, WordSpec}
import view.Tui
class TUITest extends WordSpec with Matchers {
  "The TUI" when {
    val controller = new Controller()
    val tui = new Tui(controller)
    val players:Array[String] =Array.ofDim(2)
    players(0) = "a"
    players(1) = "b"
    tui.getPlayerCount(2,players)
    "new" should {
        "have a controller" in {
          tui.getController should be (controller)
        }
    }
    "can update itself" in {
      val player = Player("X")
      val player2 = Player("P")
      val street = Street("S", 1, 1, -1, 1, 0, mortgage = false)
      val train = Trainstation("T", 2, 2, -1, 2, hypothek = false)
      tui.update(printEverythingEvent()) should be(true)
      tui.update(brokeEvent(player)) should be(true)
      tui.update(payRentEvent(player, player2)) should be(true)
      tui.update(buyStreetEvent(player, street)) should be(true)
      tui.update(buyTrainstationEvent(player, street)) should be(true)
      tui.update(playerInJailEvent(player)) should be(true)
      tui.update(normalTurnEvent(player)) should be(true)
      tui.update(diceEvent(2, 2, pasch = false)) should be(true)
      tui.update(playerSellsStreetEvent(player, street)) should be(true)
      tui.update(playerUsesHyptohekOnStreetEvent(player, street)) should be(true)
      tui.update(playerPaysHyptohekOnStreetEvent(player, street)) should be(true)
      tui.update(playerUsesHyptohekOnTrainstationEvent(player, train)) should be(true)
      tui.update(playerPaysHyptohekOnTrainstationEvent(player, train)) should be(true)
      tui.update(playerSellsTrainstationEvent(player, train)) should be(true)
      tui.update(newRoundEvent(2)) should be(true)
      tui.update(endRoundEvent(2)) should be(true)
      tui.update(playerMoveToJail(player)) should be(true)
      tui.update(optionEvent("buy")) should be(true)
      tui.update(playerMoveEvent(player)) should be(true)
      tui.update(playerIsFreeEvent(player)) should be(true)
      tui.update(playerRemainsInJailEvent(player)) should be(true)
      tui.update(playerWentOverGoEvent(player)) should be(true)
      tui.update(playerWentOnGoEvent(player)) should be(true)
      tui.update(streetOnHypothekEvent(street)) should be(true)
      tui.update(playerHasDeptEvent(player)) should be(true)
      tui.update(gameOverEvent(player))
      tui.update(falseTestEvent()) should be(false)
    }
    "can make a String with Board Information" in{
      tui.getPlayerAndBoardToString should be ("\nSpieler: name: a pos: 0 money: 10000 roundsInJail: -1\nname: b pos: 0 money: 10000 roundsInJail: -1\n\nSpielfeld:\nLos | players on this field: a b \nStrasse1 group: 1 price: 60 rent: 200 homecount: 0 mortgage: false | Owner: -1\nGemeinschaftsfeld1\nStrasse2 group: 1 price: 60 rent: 200 homecount: 0 mortgage: false | Owner: -1\nEinkommensteuer\nSuedbahnhof group: 9 price: 200 rent: 200 hypothek: false | Owner: -1\nStrasse3 group: 2 price: 100 rent: 200 homecount: 0 mortgage: false | Owner: -1\nEreignisfeld1\nStrasse4 group: 2 price: 100 rent: 200 homecount: 0 mortgage: false | Owner: -1\nStrasse5 group: 2 price: 120 rent: 200 homecount: 0 mortgage: false | Owner: -1\nZu besuch oder im Gefaengnis\nStrasse6 group: 3 price: 140 rent: 200 homecount: 0 mortgage: false | Owner: -1\nElektrizitaetswerk group: 10 price: 150 rent: 200 hypothek: false\nStrasse7 group: 3 price: 140 rent: 200 homecount: 0 mortgage: false | Owner: -1\nStrasse8 group: 3 price: 160 rent: 200 homecount: 0 mortgage: false | Owner: -1\nWestbahnhof group: 9 price: 200 rent: 200 hypothek: false | Owner: -1\nStrasse9 group: 4 price: 180 rent: 200 homecount: 0 mortgage: false | Owner: -1\nGemeinschaftsfeld2\nStrasse10 group: 4 price: 180 rent: 200 homecount: 0 mortgage: false | Owner: -1\nStrasse11 group: 4 price: 200 rent: 200 homecount: 0 mortgage: false | Owner: -1\nFreiparken\nStrasse12 group: 5 price: 220 rent: 200 homecount: 0 mortgage: false | Owner: -1\nEreignisfeld2\nStrasse13 group: 5 price: 220 rent: 200 homecount: 0 mortgage: false | Owner: -1\nStrasse14 group: 5 price: 240 rent: 200 homecount: 0 mortgage: false | Owner: -1\nNordbahnhof group: 9 price: 200 rent: 200 hypothek: false | Owner: -1\nStrasse15 group: 6 price: 260 rent: 500 homecount: 0 mortgage: false | Owner: -1\nStrasse16 group: 6 price: 260 rent: 800 homecount: 0 mortgage: false | Owner: -1\nWasserwerk group: 10 price: 150 rent: 200 hypothek: false\nStrasse17 group: 6 price: 280 rent: 2500 homecount: 0 mortgage: false | Owner: -1\nGehe ins Gefaengnis\nStrasse18 group: 7 price: 300 rent: 200 homecount: 0 mortgage: false | Owner: -1\nStrasse19 group: 7 price: 300 rent: 200 homecount: 0 mortgage: false | Owner: -1\nGemeinschaftsfeld3\nStrasse20 group: 7 price: 320 rent: 200 homecount: 0 mortgage: false | Owner: -1\nNordbahnhof group: 9 price: 200 rent: 200 hypothek: false | Owner: -1\nEreignisfeld3\nStrasse21 group: 8 price: 350 rent: 200 homecount: 0 mortgage: false | Owner: -1\nZusatzsteuer\nStrasse22 group: 8 price: 400 rent: 200 homecount: 0 mortgage: false | Owner: -1\n")
    }
    "can print Dice counts" in {
      tui.getRollString(diceEvent(1,1,pasch = true)) should be("throwing Dice:\n" +
        "rolled :1 1\n" +
        "rolled pasch!")
      tui.getRollString(diceEvent(2,1,pasch = false)) should be("throwing Dice:\n" +
        "rolled :2 1\n")
    }
    "can make normal Turn String" in{
      val player = Player("X")
      tui.getNormalTurnString(normalTurnEvent(player)) should be("Its X turn!\n")
    }
    "can make String if a player is in jail" in{
      val player = Player("X")
      tui.getPlayerInJailString(playerInJailEvent(player)) should be("\nIts X turn. he is in jail!\n" +
        "Jailcount: 0\n")
    }
    "can make String if a player is free" in{
      val player = Player("X")
      tui.getPlayerIsFreeString(playerIsFreeEvent(player)) should be("X is free again!")
    }
    "can make String if player remains in jail" in{
      val player = Player("X")
      tui.getPlayerRemainsInJailString(playerRemainsInJailEvent(player)) should be ("X remains in jail")
    }
    "can make String if a street is on mortgage"in{
      val street = Street("S",1,1,-1,1,0,mortgage = true)
      tui.getStreetOnHypothekString(streetOnHypothekEvent(street)) should be ("S is on hypothek.")
    }
    "can make String if a player buys a Street" in{
      val player = Player("X")
      val street = Street("S",1,1,-1,1,0,mortgage = false)
      val street2 = Street("S",1,99999999,-1,1,0,mortgage = false)
      tui.getBuyStreetEventString(buyStreetEvent(player,street)) should be("10000\nbought S")
      tui.getBuyStreetEventString(buyStreetEvent(player,street2)) should be(player.money+"\ncan´t afford street")
    }
    "can make String if a player pays rent" in{
      val player = Player("X")
      val player2 = Player("S")
      tui.getPayRentString(payRentEvent(player,player2)) should be("X pays rent to S")
    }
    "can make String if player goes over go" in{
      val player = Player("X")
      tui.getPlayerWentOverGoString(playerWentOverGoEvent(player)) should be ("X went over go.")
    }
    "can make String if player goes on go" in{
      val player = Player("X")
      tui.getPlayerWentOnGoString(playerWentOnGoEvent(player)) should be ("X went on go and gets extra money.")
    }
    "can make String game is over" in{
      val player = Player("X")
      tui.getGameOverString(gameOverEvent(player)) should be("X is the winner!!")
    }
    "can make String if player is broke" in{
      val player = Player("X")
      tui.getBrokeEventString(brokeEvent(player)) should be("X is broke!!")
    }
    "can make String if player uses Mortgage" in{
      val player = Player("X")
      val street = Street("S",1,1,-1,1,0,mortgage = false)
      tui.getPlayerUsesHypothekOnStreetString(playerUsesHyptohekOnStreetEvent(player,street)) should be ("X gets Hypothek for S new creditbalance: 10000")
    }
    "can make String if player pays hypothek"in{
      val player = Player("X")
      val street = Street("S",1,1,-1,1,0,mortgage = false)
      tui.getPlayerPaysHypothekOnStreetString(playerPaysHyptohekOnStreetEvent(player,street)) should be ("X pays Hypothek for S new creditbalance: 10000")
    }
    "can make String if player sells Street"in{
      val player = Player("X")
      val street = Street("S",1,1,-1,1,0,mortgage = false)
      tui.getPlayerSellsStreetString(playerSellsStreetEvent(player,street)) should be ("X sells S\nnew creditbalance: 10000")
    }
    "can make String if round ends"in{
      tui.getEndRoundString(endRoundEvent(2)) should be ("\n\n\nround 2 ends")
    }
    "can make String if new round starts"in{
      tui.getNewRoundString(newRoundEvent(3))should be ("\n\nround 3 starts")
    }
    "can make String if player moves to jail"in{
      val player = Player("X")
      tui.getPlayerMoveToJailString(playerMoveToJail(player)) should be("X moved to Jail!")
    }
    "can make String if player moves"in{
      val player = Player("X")
      tui.getPlayerMovedString(playerMoveEvent(player))should be ("X moved to 0")
    }
    "can make String if player has Depth"in{
      val player = Player("X")
      tui.getPlayerHasDeptEventString(playerHasDeptEvent(player)) should be ("X is in minus: 10000")
    }
    "can make Strings for Options"in{
      tui.getOptionString(optionEvent("buys")) should be ("option: buys")
    }
  }
}
