import controller.Controller
import model.{Player, diceEvent, normalTurnEvent}
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
    "can make a String with Board Information" in{
      tui.getPlayerAndBoardToString should be ("\nSpieler: name: a pos: 0 money: 10000 roundsInJail: -1\nname: b pos: 0 money: 10000 roundsInJail: -1\n\nSpielfeld:\nLos | players on this field: a b \nStrasse1 group: 1 price: 60 rent: 200 homecount: 0 mortgage: false | Owner: -1\nGemeinschaftsfeld1\nStrasse2 group: 1 price: 60 rent: 200 homecount: 0 mortgage: false | Owner: -1\nEinkommensteuer\nSuedbahnhof group: 9 price: 200 rent: 200 hypothek: false | Owner: -1\nStrasse3 group: 2 price: 100 rent: 200 homecount: 0 mortgage: false | Owner: -1\nEreignisfeld1\nStrasse4 group: 2 price: 100 rent: 200 homecount: 0 mortgage: false | Owner: -1\nStrasse5 group: 2 price: 120 rent: 200 homecount: 0 mortgage: false | Owner: -1\nZu besuch oder im Gefaengnis\nStrasse6 group: 3 price: 140 rent: 200 homecount: 0 mortgage: false | Owner: -1\nElektrizitaetswerk group: 10 price: 150 rent: 200 hypothek: false\nStrasse7 group: 3 price: 140 rent: 200 homecount: 0 mortgage: false | Owner: -1\nStrasse8 group: 3 price: 160 rent: 200 homecount: 0 mortgage: false | Owner: -1\nWestbahnhof group: 9 price: 200 rent: 200 hypothek: false | Owner: -1\nStrasse9 group: 4 price: 180 rent: 200 homecount: 0 mortgage: false | Owner: -1\nGemeinschaftsfeld2\nStrasse10 group: 4 price: 180 rent: 200 homecount: 0 mortgage: false | Owner: -1\nStrasse11 group: 4 price: 200 rent: 200 homecount: 0 mortgage: false | Owner: -1\nFreiparken\nStrasse12 group: 5 price: 220 rent: 200 homecount: 0 mortgage: false | Owner: -1\nEreignisfeld2\nStrasse13 group: 5 price: 220 rent: 200 homecount: 0 mortgage: false | Owner: -1\nStrasse14 group: 5 price: 240 rent: 200 homecount: 0 mortgage: false | Owner: -1\nNordbahnhof group: 9 price: 200 rent: 200 hypothek: false | Owner: -1\nStrasse15 group: 6 price: 260 rent: 500 homecount: 0 mortgage: false | Owner: -1\nStrasse16 group: 6 price: 260 rent: 800 homecount: 0 mortgage: false | Owner: -1\nWasserwerk group: 10 price: 150 rent: 200 hypothek: false\nStrasse17 group: 6 price: 280 rent: 2500 homecount: 0 mortgage: false | Owner: -1\nGehe ins Gefaengnis\nStrasse18 group: 7 price: 300 rent: 200 homecount: 0 mortgage: false | Owner: -1\nStrasse19 group: 7 price: 300 rent: 200 homecount: 0 mortgage: false | Owner: -1\nGemeinschaftsfeld3\nStrasse20 group: 7 price: 320 rent: 200 homecount: 0 mortgage: false | Owner: -1\nNordbahnhof group: 9 price: 200 rent: 200 hypothek: false | Owner: -1\nEreignisfeld3\nStrasse21 group: 8 price: 350 rent: 200 homecount: 0 mortgage: false | Owner: -1\nZusatzsteuer\nStrasse22 group: 8 price: 400 rent: 200 homecount: 0 mortgage: false | Owner: -1\n")
    }
    "can print Dice counts" in {
      tui.getRollString(diceEvent(1,1,true)) should be("throwing Dice:\n" +
        "rolled :1 1\n" +
        "rolled pasch!")
      tui.getRollString(diceEvent(2,1,false)) should be("throwing Dice:\n" +
        "rolled :2 1\n")
    }
    "can make normal Turn String" in{
      val player = Player("X")
      tui.getNormalTurnString(normalTurnEvent(player)) should be("Its X turn!\n")
    }
  }
}
