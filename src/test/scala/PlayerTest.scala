import model.Player
import org.scalatest._

class PlayerTest extends WordSpec with Matchers {
  "A Player" when { "new" should {
    val player = Player("Önder")
    "have a name" in {
      player.name should be("Önder")
    }
    "have a cool String representation" in {
      player.toString should be("name: Önder pos: 0 money: 20000")
    }
    "Have the Position 0" in {
      player.position should be(0)
    }
    "Have the start capacity " in {
      player.money should be (20000)
    }
  }
    "set to a specific Position"should{
      val player = Player("Name",5)
      "return that value" in{
        player.position should be(5)
      }
    }
    "gains or loses money" should{
      val player = Player("Name")
      val player2 = player.incMoney(500)
      val player3 = player.decMoney(500)
      "return the amount of money he has" in{
        player2.money should be(player.money+500)
        player3.money should be(player.money-500)
      }
    }
    "moved to Start or Jail" should{
      val player = Player("Name")
      val player2 = player.moveToStart
      val player3 = player.moveToJail
      "return the position 0" in{
        player2.position should be (0)
      }
      "return the position 5" in{
        player3.position should be (5)
      }
    }

}
}
