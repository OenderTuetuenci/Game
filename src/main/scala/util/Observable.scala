package util

import model.Event

trait Observer{
  def update(z:Event) :Unit
}

class Observable {
    var subscribers: Vector[Observer] = Vector()
    def add(s: Observer): Unit = subscribers = subscribers :+ s
    def remove(s: Observer): Unit = subscribers = subscribers.filterNot(o => o == s)
    def notifyObservers(z:Int): Unit = subscribers.foreach(o => o.update(z))
}
