import model.Dice
import org.scalatest.{Matchers, WordSpec}


class DiceTest extends WordSpec with Matchers{
  "A Dice" when { "throwen" should{
    val dice = Dice()
    val eyecount = dice.throwDice
    "have a number between 1 and 6" in {
      eyecount should be > 0
      eyecount should be < 7
    }
  }
    "can check" should {
      val dice = Dice()
      val x = dice.throwDice(1)
      val z = dice.throwDice(2)
      "if it has a Pash with another Dice" in{
        dice.checkPash(x,x) should be (true)
        dice.checkPash(x,z) should be (false)
      }
    }
  }
}
