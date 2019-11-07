import model.{Dice, Player}

println("hallo")

case class Default(x:Int){
  def add(y:Int) : Int = x+y
}
val d = Default(5)
val b = d.add(5)


42.toBinaryString
42.toHexString
scala.math.sqrt(9)
val bb = 5

val ss = "Önder Tütünci"
ss.indexOf("P")
val t = ss.splitAt(5)
var player = Player("Önder")
player = player.move(5)
println(player)

class test{

}
class test2(name:String) extends test{
  def getName : String = name
}

val array = new Array[test](3)
array(0) = new test2("hallo")
println(array(0))
val z = Player("Name")
z.toString
