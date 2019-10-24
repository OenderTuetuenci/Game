import model.Player

object Game {
  def main(args: Array[String]): Unit = {
    val player = Player("Önder")
    println("Hello, "+player.name)

    val player2 = Player("Vincent")
    println("Hello, "+player.name)
  }
}


