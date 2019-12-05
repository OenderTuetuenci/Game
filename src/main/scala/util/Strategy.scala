package util

import controller.GameController
import model.{askBuyEvent, askBuyHomeEvent}

trait Strategy {
    val controller:GameController
    def execute(option: String)
}
case class NPCStrategy(controller: GameController) extends Strategy {
  override def execute(option: String): Unit = {
    option match {
      case "pay" => pay
      case "buy" => buy
      case "buy home" => buyHome
      case _ =>
    }

    def buyHome: Unit = {
      controller.buyHome
    }

    def buy: Unit = {
      controller.buy
    }

    def pay: Unit = {
      controller.payRent
    }
  }
}
case class HumanStrategy(controller: GameController) extends Strategy{
  override def execute(option: String): Unit = {
    option match {
      case "pay" => pay
      case "buy" => buy
      case "buy home" => buyHome
      case _ =>
    }
    def buyHome :Unit = {
      controller.notifyObservers(askBuyHomeEvent())
      if(controller.answer == "yes")
        controller.buyHome
    }
    def buy :Unit = {
      controller.notifyObservers(askBuyEvent())
      if(controller.answer == "yes")
        controller.buy
    }
    def pay :Unit = {
      controller.payRent
    }
  }
}
