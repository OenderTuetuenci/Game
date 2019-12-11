package controller

import model.{OpenRollDiceDialogEvent, OpenRollForPosDialogEvent, askBuyEvent, askBuyHomeEvent}

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
      case "rollForPosition" => controller.playerController.wuerfeln
      case "rollDice" => controller.playerController.wuerfeln
      case "turnInJail" => turnInJail
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

  def turnInJail: String = {
    "rollDice" // todo logik für bot im jail hier
  }

}

case class HumanStrategy(controller: GameController) extends HumanOrNpcStrategy {
  override def execute(option: String): Any = {
    option match {
      case "pay" => pay
      case "buy" => buy
      case "buy home" => buyHome
      case "turnInJail" => turnInJail
      case "rollForPosition" =>
        controller.notifyObservers(OpenRollForPosDialogEvent(controller.currentStage, controller.players(controller.isturn)))
        controller.playerController.wuerfeln
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

  def pay: Unit = {
    controller.payRent
  }

  def turnInJail: String = {
    //controller.notifyObservers(OpenInJailDialogEvent(controller.currentStage, controller.players(controller.isturn)))
    "rollDice"
  }
}
