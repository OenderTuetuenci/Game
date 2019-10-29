import model.Cell
import model.Street
import org.scalatest.{Matchers, WordSpec}

class CellTest extends WordSpec with Matchers {
  "A Street" when { "new" should{
    val cell = Street("mystreet",1,3000,null,1,0) // NO NULL ( option )
    "have a name length " in {
      cell.name.length should be > 0
      cell.name.length should be < 20
    }
    " should have cool String representation" in{
      cell.toString() should be (cell.toString)
    }
  }

  }
}
