import controller.Controller
import org.scalatest.{Matchers, WordSpec}
import view.Tui

class ControllerTest extends WordSpec with Matchers{ {
  "A Controller" when {
    val controller = new Controller()
    val tui = new Tui(controller)
    val players:Array[String] =Array.ofDim(2)
    players(0) = "a"
    players(1) = "b"
    tui.getPlayerCount(2,players)
  }
}
}
