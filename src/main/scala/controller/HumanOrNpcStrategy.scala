package controller

import com.google.inject.Inject
import com.google.inject.name.Named
import controller.controllerComponent.GameController
import model.{OpenRollForPosDialogEvent, askBuyEvent, askBuyHomeEvent}

trait HumanOrNpcStrategy {
  val controller: GameControllerInterface
  def execute(option: String): Any
}

case class NPCStrategy @Inject() (controller: GameControllerInterface) extends HumanOrNpcStrategy {
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

case class HumanStrategy @Inject() (controller: GameControllerInterface) extends HumanOrNpcStrategy {
  override def execute(option: String): Any = {
    option match {
      case "pay" => pay
      case "buy" => buy
      case "buy home" => buyHome
      case "turnInJail" => turnInJail
      case "rollForPosition" =>
        controller.notifyObservers(OpenRollForPosDialogEvent(controller.players(controller.currentPlayer)))
        controller.playerController.wuerfeln
      case "rollDice" =>
          //controller.notifyObservers(OpenRollDiceDialogEvent(controller.players(controller.currentPlayer)))
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
      //controller.notifyObservers(OpenInJailDialogEvent(controller.currentStage, controller.players(controller.currentPlayer)))
    "rollDice"
  }
}
