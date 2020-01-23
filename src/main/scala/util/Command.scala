package util

trait Command {
  def doStep: Unit

  def redoStep: Unit

  def undoStep: Unit
}