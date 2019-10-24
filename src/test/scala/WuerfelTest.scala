import model.Wuerfel
import org.scalatest.{Matchers, WordSpec}


class WuerfelTest extends WordSpec with Matchers{
  "A Wuerfel" when { "new" should{
    val wuerfel = Wuerfel()
    "have a number between 1 and 6" in {
      wuerfel.eyecount should be > 0
      wuerfel.eyecount should be < 7
    }
    "when thrown should have the same or a different Number between 1 and 6 " in {
      val newWuerfel = wuerfel.throwDice()
      wuerfel.eyecount should be > 0
      wuerfel.eyecount should be < 7
    }
  }
  }

}
