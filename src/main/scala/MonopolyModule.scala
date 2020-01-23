package Game

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import controller.controllerComponent.ControllerInterface
import controller.controllerComponent.controllerBaseImpl.Controller
import model.DiceComponent.Dice
import model.fileIOComponent._
import model.playerComponent.Player
import model.{Cards, CardsInterface, DiceInterface, PlayerInterface}
import net.codingwell.scalaguice.ScalaModule

class MonopolyModule extends AbstractModule with ScalaModule {
    val defaultName: String = ""
    val defaultPosition: Int = 0
    val defaultMoney: Int = 1000
    val defaultJailCount: Int = 0
    val defaultTurnPosition: Int = 0
    val defaultRollForPosition: Int = 0
    val defaultFigure = ""
    val defaultIsNpc: Boolean = false

    override def configure(): Unit = {
        bindConstant().annotatedWith(Names.named("name")).to(defaultName)
        bindConstant().annotatedWith(Names.named("position")).to(defaultPosition)
        bindConstant().annotatedWith(Names.named("money")).to(defaultMoney)
        bindConstant().annotatedWith(Names.named("jailCount")).to(defaultJailCount)
        bindConstant().annotatedWith(Names.named("turnPosition")).to(defaultTurnPosition)
        bindConstant().annotatedWith(Names.named("rollForPosition")).to(defaultRollForPosition)
        bindConstant().annotatedWith(Names.named("figure")).to(defaultFigure)
        bindConstant().annotatedWith(Names.named("isNpc")).to(defaultIsNpc)

        bind[ControllerInterface].to[Controller]
        bind[DiceInterface].to[Dice]
        bind[CardsInterface].to[Cards]
        bind[PlayerInterface].to[Player]
        //bind[FileIOInterface].to[fileIOJsonImpl.FileIO]
        bind[FileIOInterface].to[fileIOXmlImpl.FileIO]
    }
}