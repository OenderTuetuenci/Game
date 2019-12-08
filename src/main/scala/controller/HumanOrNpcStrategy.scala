package controller

import Game.Monopoly._
import model.{askBuyEvent, askBuyHomeEvent}

trait HumanOrNpcStrategy {
  val controller: GameController

  def execute(option: String)
}

case class NPCStrategy(controller: GameController) extends HumanOrNpcStrategy {
  override def execute(option: String): Unit = {
    option match {
      case "pay" => pay
      case "buy" => buy
      case "buy home" => buyHome
      case "rollDice" => rollDice
      case _ =>
    }

    def rollDice(): (Int, Int, Boolean) = {
      val ret = gameController.playerController.wuerfeln
      print(ret)
      ret
    }
    //                    val ret =  DialogStrategy.rollDiceDialog(currentStage, player)

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

case class HumanStrategy(controller: GameController) extends HumanOrNpcStrategy {
  override def execute(option: String): Unit = {
    option match {
      case "pay" => pay
      case "buy" => buy
      case "buy home" => buyHome
      case "rollDice" => rollDice
      case _ =>
    }

    def rollDice(): (Int, Int, Boolean) = {
      val ret = DialogStrategy.rollDiceDialog(gameController.currentStage, gameController.players(gameController.isturn))
      print(ret)
      ret
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
}
