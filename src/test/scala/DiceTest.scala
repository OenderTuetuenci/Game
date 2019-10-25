import model.Dice
import org.scalatest.{Matchers, WordSpec}


class DiceTest extends WordSpec with Matchers{
  "A Wuerfel" when { "throwen" should{
    val wuerfel = Dice()
    val eyecount = wuerfel.throwDice()
    "have a number between 1 and 6" in {
      eyecount should be > 0
      eyecount should be < 7
    }
  }
    "can check" should {
      val dice = Dice()
      val x = 1
      val z = 3
      "if it has a Pash with another Dice" in{
        dice.checkPash(x,x) should be (true)
        dice.checkPash(x,z) should be (false)
      }
    }
  }
}
