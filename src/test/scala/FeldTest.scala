import model.Feld
import model.Strasse
import org.scalatest.{Matchers, WordSpec}

class FeldTest extends WordSpec with Matchers {
  "A Street" when { "new" should{
    val strasse = Strasse("mystreet",1,3000,null,1) // NO NULL ( option )
    "have a name length " in {
      strasse.name.length should be > 0
      strasse.name.length should be < 20
    }
    " should have cool String representation" in{
      strasse.toString() should be (strasse.toString)
    }
  }

  }
}
