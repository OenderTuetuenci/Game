package util

import controller.GameController

trait Command {
  def doStep:Unit
  def redoStep:Unit
  def undoStep:Unit
}

class createGameCommand(controller: GameController,npcNames:Array[String],playerNames:Array[String]) extends Command{
  override def doStep: Unit = controller.createGame(playerNames,npcNames)
  override def redoStep: Unit = controller.createGame(playerNames,npcNames)
  override def undoStep: Unit = controller.createGame(Array[String](),Array[String]())
}

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
