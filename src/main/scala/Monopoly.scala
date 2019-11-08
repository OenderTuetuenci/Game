import controller.Controller
import view.Tui

object Monopoly {
  val controller = new Controller()
  val tui = new Tui(controller)
  def main(args: Array[String]): Unit = {
    tui.getPlayerCount()
    controller.notifyObservers
    //todo
    //do{
    //
    //}while(controller.gameOver)
  }
}
