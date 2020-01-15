package model.fileIOComponent

import controller.controllerComponent.GameControllerInterface

trait FileIOInterface {
    def saveGame(game:GameControllerInterface): Unit

    def loadGame: Unit

    //def saveBoard:Unit
    //def loadBoard:Unit
}
