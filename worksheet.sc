import model.Dice

println("hallo")

case class Default(x:Int){
  def add(y:Int) : Int = x+y
}
val d = Default(5)
val b = d.add(5)


42.toBinaryString
42.toHexString
scala.math.sqrt(9)
val b = 5

val s = "Önder Tütünci"
s.indexOf("P")
val t = s.splitAt(5)