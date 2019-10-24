import model.{Player, Wuerfel}

object Game {
  def main(args: Array[String]): Unit = {
    val player = Player("Önder")
    println(player)

    var dice = Wuerfel()
    dice = dice.throwDice()
    dice.checkPash(dice)
    println(dice)
  }
}


