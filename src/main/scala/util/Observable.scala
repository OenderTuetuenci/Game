package util

import model._

trait Observer{
    def update(e: PrintEvent): Boolean

    //todo updateOthers????
}

class Observable {
    var subscribers: Vector[Observer] = Vector()
    def add(s: Observer): Unit = subscribers = subscribers :+ s
    def remove(s: Observer): Unit = subscribers = subscribers.filterNot(o => o == s)

    def notifyObservers(e: PrintEvent): Unit = subscribers.foreach(o => o.update(e))
}
