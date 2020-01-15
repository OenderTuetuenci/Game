package model.fileIOComponent.fileIOXmlImpl

import controller.controllerComponent.GameControllerInterface
import model.{Cell, PlayerInterface}
import model.fileIOComponent.FileIOInterface

class FileIO extends FileIOInterface {
    override def saveGame(game: GameControllerInterface) = {
        ;
    }

    override def loadGame: (Int,Int,Int,Int,Int,Vector[Cell],Vector[PlayerInterface],Vector[String],Vector[String],Int) = {
        null
    }
}
