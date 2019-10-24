import model.{Player, Dice}

object Game {
  def main(args: Array[String]): Unit = {
    val player = Player("Önder")
    println(player)

    var dice = Dice()
    dice = dice.throwDice()
    dice.checkPash(dice)
    println(dice)
  }
}


