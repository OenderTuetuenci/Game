package model.fileIOComponent

import controller.controllerComponent.GameControllerInterface
import model.{Cell, PlayerInterface}

trait FileIOInterface {
    def saveGame(game:GameControllerInterface): Unit

    def loadGame: (Int,Int,Int,Int,Int,Vector[Cell],Vector[PlayerInterface],Vector[String],Vector[String],Int)

    //def saveBoard:Unit
    //def loadBoard:Unit
}
