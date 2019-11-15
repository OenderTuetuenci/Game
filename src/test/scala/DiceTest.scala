import model.Dice
import org.scalatest.{Matchers, WordSpec}


class DiceTest extends WordSpec with Matchers{
  "A Dice" when { "throwen" should{
      val dice = Dice()
      val roll = dice.roll
    "have a number between 1 and 6" in {
      roll should be > 0
      roll should be < 7
    }
  }
    "can check" should {
        val dice = Dice()
        val x = 1
        val z = 2
      "if it has a Pash with another Dice" in{
        dice.checkPash(x,x) should be (true)
        dice.checkPash(x,z) should be (false)
      }
    }
  }
}
