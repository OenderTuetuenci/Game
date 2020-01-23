package controller.controllerComponent.controllerBaseImpl

import controller.controllerComponent.ControllerInterface
import util.Command

class getPlayernameAndFigureCommand(controller: ControllerInterface, currentPlayer: Int,
                                    playerNames: Vector[String], playerFigures: Vector[String],
                                    remainingFigures: List[String]) extends Command {
    override def doStep: Unit = {
        controller.currentPlayer = currentPlayer
        controller.playerNames = playerNames
        controller.playerFigures = playerFigures
        controller.remainingFiguresToPick = remainingFigures
    }

    override def undoStep: Unit = {
        controller.currentPlayer = currentPlayer - 1 // muss -1 da im loop hochgezaehlt wird !
        controller.playerNames = playerNames
        controller.playerFigures = playerFigures
        controller.remainingFiguresToPick = remainingFigures
    }

    override def redoStep: Unit = {
        controller.currentPlayer = currentPlayer
        controller.playerNames = playerNames
        controller.playerFigures = playerFigures
        controller.remainingFiguresToPick = remainingFigures
    }
}