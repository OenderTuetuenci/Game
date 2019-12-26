package Game

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import controller.{GameControllerInterface, HumanOrNpcStrategy, HumanStrategy, NPCStrategy}
import controller.controllerComponent.GameController
import model.DiceComponent.Dice
import model.{DiceInterface, PlayerInterface}
import model.playerComponent.Player
import net.codingwell.scalaguice.ScalaModule
import scalafx.scene.image.ImageView

class MonopolyModule extends AbstractModule with ScalaModule{
  val defaultPosition:Int = 0
  val defaultMoney:Int = 1000
  val defaultJailCount:Int = 0
  val defaultOwnedStreets:Vector[Int] = Vector[Int]()
  val defaultTurnPosition:Int = 0
  val defaultRollForPosition:Int = 0
  override def configure():Unit ={
    bindConstant().annotatedWith(Names.named("position")).to(defaultPosition)
    bindConstant().annotatedWith(Names.named("money")).to(defaultMoney)
    bindConstant().annotatedWith(Names.named("jailCount")).to(defaultJailCount)
    bindConstant().annotatedWith(Names.named("turnPosition")).to(defaultTurnPosition)
    bindConstant().annotatedWith(Names.named("rollForPosition")).to(defaultRollForPosition)
    bindConstant().annotatedWith(Names.named("ownedStreets")).to(0)
    bind[GameControllerInterface].to[GameController]
    bind[DiceInterface].to[Dice]
    //bind[PlayerInterface].to[Player]//Funktionier mit vector und Imageview nicht online gibts keine hilfe
  }
}
