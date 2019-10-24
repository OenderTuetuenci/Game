import model.Dice
import org.scalatest.{Matchers, WordSpec}


class DiceTest extends WordSpec with Matchers{
  "A Wuerfel" when { "new" should{
    val wuerfel = Dice()
    "have a number between 1 and 6" in {
      wuerfel.eyecount should be > 0
      wuerfel.eyecount should be < 7
    }
  }
    "created" should {
      val dice = Dice()
      "have a Number between 1 and 6" in{
        val n = dice.throwDice()
        n.eyecount should be > 0
        n.eyecount should be < 7
      }
      "have a String represantion" in{
        dice.toString should be (dice.eyecount.toString)
      }
      "can if it has a Pash with another Dice" in{
        dice.checkPash(dice) should be (true)
      }
    }
  }
}
