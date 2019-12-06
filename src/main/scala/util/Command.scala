package util

import controller.GameController
import model.newGameEvent

trait Command {
  def doStep:Unit
  def redoStep:Unit
  def undoStep:Unit
}

class createGameCommand(controller: GameController,npcNames:Array[String],playerNames:Array[String]) extends Command{
  override def doStep: Unit = controller.createGame(npcNames,playerNames)
  override def redoStep: Unit = controller.createGame(npcNames,playerNames)
  override def undoStep: Unit = controller.notifyObservers(newGameEvent())
}

//todo runround undo
class createRunRoundCommand()

class UndoManager{
  private var undoStack:List[Command] = Nil
  private var redoStack:List[Command] = Nil

  def doStep(command: Command): Unit ={
    undoStack = command :: undoStack
    command.doStep
  }
  def undoStep = {
    undoStack match {
      case Nil =>
      case head::stack => {
        head.undoStep
        undoStack = stack
        redoStack = head::redoStack
      }
    }
  }

}
