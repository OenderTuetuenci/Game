import controller.Controller
import model.printEverythingEvent
import view.Tui

object Monopoly {
  val controller = new Controller()
  val tui = new Tui(controller)
  def main(args: Array[String]): Unit = {
    tui.getPlayerCount
    controller.notifyObservers(printEverythingEvent())
    do{
      controller.runRound()
    }while(!controller.gameOver)
  }
}
