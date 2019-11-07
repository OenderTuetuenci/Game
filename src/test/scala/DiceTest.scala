import model.Dice
import org.scalatest.{Matchers, WordSpec}


class DiceTest extends WordSpec with Matchers{
  "A Dice" when { "throwen" should{
      val dice = Dice(0)
      val eyecount = dice.eyeCount
    "have a number between 1 and 6" in {
      eyecount should be > 0
      eyecount should be < 7
    }
  }
    "can check" should {
        val dice = Dice(0)
        val x = dice.eyeCount
        val z = dice.eyeCount
      "if it has a Pash with another Dice" in{
        dice.checkPash(x,x) should be (true)
        dice.checkPash(x,z) should be (false)
      }
    }
  }
}
