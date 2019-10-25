import model.{Player, Dice}

object Game {
  def main(args: Array[String]): Unit = {
    val player = Player("Önder")
    println(player)

    val dice = Dice()
    val x = dice.throwDice()
    println(dice.checkPash(x,x))
  }
}


