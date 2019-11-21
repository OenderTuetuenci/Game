import model.Player
import org.scalatest._

class PlayerTest extends WordSpec with Matchers {
  "A Player" when { "new" should {
    val player = Player("Önder")
    "have a name" in {
      player.name should be("Önder")
    }
    "have a cool String representation" in {
      player.toString should be("name: Önder pos: 0 money: 10000 roundsInJail: -1")
    }
    "Have the Position 0" in {
      player.position should be(0)
    }
    "Have the start capacity " in {
      player.money should be (10000)
    }
    "Have the jailcount" in {
      player.jailCount should be(-1)
    }
  }
    "can move his position" should{
      val player = Player("Name",40)
      val player2 = Player("Name")
      val forward = player.move(5)
      val backward = player2.moveBack(5)
      "can move forward" in {
        forward.position should be(45)
      }
      "can move backwards" in{
        backward.position should be(-5)
      }
    }
    "set to a specific Position"should{
      val player = Player("Name",5)
      "return that value" in{
        player.position should be(5)
      }
    }
    "set to a new name" should{
      val player = Player("Name")
      "return new name" in{
        player.name should be("Name")
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
      "return the position 10" in{
        player3.position should be (10)
      }
    }
    "knows his own jailtime" should{
      val player = Player("Name")
      val player2 = player.incJailTime
      val player3 = player2.resetJailCount
      "if he has jailtime return a number >= 0" in{
        player2.jailCount should be >= 0
      }
      "if he returns from jail return -1" in {
        player3.jailCount should be (-1)
      }
    }

}
}
