import model.Player
import org.scalatest._

class PlayerTest extends WordSpec with Matchers {
  "A Player" when { "new" should {
    val player = Player("Önder")
    "have a name" in {
      player.name should be("Önder")
    }
    "have a cool String representation" in {
      player.toString should be("Önder")
    }
  }
}}
