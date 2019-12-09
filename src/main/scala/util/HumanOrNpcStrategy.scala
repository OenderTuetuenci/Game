package util

import controller.GameController
import model.{OpenRollDiceDialogEvent, askBuyEvent, askBuyHomeEvent}

trait HumanOrNpcStrategy {
  val controller: GameController

  def execute(option: String): Any
}

case class NPCStrategy(controller: GameController) extends HumanOrNpcStrategy {
  override def execute(option: String): Any = {
    option match {
      case "pay" => pay
      case "buy" => buy
      case "buy home" => buyHome
      case "rollDice" => controller.playerController.wuerfeln
      case _ =>
    }
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

case class HumanStrategy(controller: GameController) extends HumanOrNpcStrategy {
  override def execute(option: String): Any = {
    option match {
      case "pay" => pay
      case "buy" => buy
      case "buy home" => buyHome
      case "rollDice" =>
        controller.notifyObservers(OpenRollDiceDialogEvent(controller.currentStage, controller.players(controller.isturn)))
        controller.playerController.wuerfeln
      case _ =>
    }
  }

    def buyHome: Unit = {
      controller.notifyObservers(askBuyHomeEvent())
      if (controller.answer == "yes")
        controller.buyHome
    }

    def buy: Unit = {
      controller.notifyObservers(askBuyEvent())
      if (controller.answer == "yes")
        controller.buy
    }
    def pay :Unit = {
      controller.payRent
    }
}
