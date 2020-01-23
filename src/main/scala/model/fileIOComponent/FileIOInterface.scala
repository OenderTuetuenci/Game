package model.fileIOComponent

import controller.controllerComponent.ControllerInterface
import model.{Cell, PlayerInterface}

trait FileIOInterface {
    def saveGame(game: ControllerInterface): Unit

    def loadGame: (Int,Int,Int,Int,Int,Vector[Cell],Vector[PlayerInterface],Vector[String],Vector[String],Int)

}
