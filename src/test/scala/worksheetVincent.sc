//println("hello world")
//5.+(10) // int
//
//5.+(10.5) // double
//5.+(10.5).toInt.+(2.5)  // int0
//
//false
//
//val c = 'a'
//
//val a = "abcdef"
//
//a + c
//
//(c + 1).toChar // "c" + 1 -> 98 ( asciiwert von c + 1)
//98.toChar
//'d' - 'c'
//c.toString + a
//
//a * 3
//
//val tripel = "hi" -> 42 -> 3.0 -> 23.0
//
//tripel._1
//tripel._2
//tripel._1._1
//tripel._1._1._2
//tripel._1._2
//
//1024.toBinaryString
//
//32.toBinaryString
//
//Byte.MinValue
//
//Byte.MaxValue
//
//Int.MaxValue
//
//Int.MinValue
//
//Char.MinValue
//Char.MaxValue.toInt
//Pow(2,2)
//2^10

println("hallo")

class Wuerfel(val augen:Int){

}

42.toBinaryString
42.toHexString
scala.math.sqrt(9)
val abv = 5

val s = "Önder Tütünci"
s.indexOf("P")
val t = s.splitAt(5)

/////////////////////////////
val c = 2
val d = 1


val a = if (c > d) "yo" else "no" // expression

if (1 > 2) print(1)
else print(2)//statement

val vb = if (c < d) {
  "a"
} else if (c > d) {
  "b"
}


print(a, vb)

///

val f = (x:Double,y:Double) => x * y
f(2,3)

val f2 = (_:Double) * 2

f2(3)

///

val lst = List(1,2,3,4,5,6,7,8)

for ( i <- lst) println(i)
val ab = Array(3,2,5)
println(ab)


for ( i <- 0 to 9) println(i)
for ( i <- ab) println(i)

// generator
val nums = Seq(1,2,3)
val letters = Seq('a', 'b', 'c')
val res = for {
  n <- nums
  c <- letters
} yield (n, c)


// guards
for {
  i <- 1 to 10
  if i > 3
  if i < 6
  if i % 2 == 0
} println(i)

//// for with yield
//val nums2 = Seq(1,2,3)
//val letters2 = Seq('a', 'b', 'c')
//val res2 = for {
//  n <- nums2
//  c <- letters2
//} yield (n, c)
//
//res2: Seq[(Int, Char)] = List((1,a), (1,b), (1,c),
//  (2,a), (2,b), (2,c),
//  (3,a), (3,b), (3,c))

//foreach
val v = Vector((1,9), (2,8), (3,7), (4,6), (5,5))
v.foreach{ case(i,j) => println(i, j) }

//// Maps each element of a list to a new element of a list according to some function
//val n = (1 to 3).toList
//n.map(i => i*3)
//List[Int] = List(3, 6, 9))
//n.map(i => n.map(j => i * j))
//List[List[Int]] = List(List(1, 2, 3), List(2, 4, 6), List(3, 6, 9))
//
//
////Produces a not-nested list (flat)
//n.flatMap(i => n.map(j => i * j))
//List[Int] = List(1, 2, 3, 2, 4, 6, 3, 6, 9)
//
////Match is similar to switch but more general
//def describe(x: Int) =
//  x match {
//    case 1 => "one"
//    case 2 => "two"
//    case _ => "many"
//  }
//
//// Variable renaming and guards
//def describe(x: Int) =
//  x match {
//    case n:Int if (n < 0) => n + " is negative"
//    case z:Int if (z ==0) => z + " is zero"
//    case p:Int if (p > 0) => p + " is positive"
//  }


//The match can be used to cast types to new variables todo für felder z.b
def describe(x: Any) =
  x match {
    case i:Int     => i + " is an Integer"
    case s:String  => s + " is a String"
    case b:Boolean => b + " is a Boolean"
    case _         => x + " has some other type"
  }


//todo
//Lists can be taken apart by pattern matching
//  elem::list = 2::5::1::Nil
//elem=2, list=5::1::Nil
//Isort with pattern matching
//def isort(list: List[Int]): List[Int] = list match {
//  case Nil => Nil
//  case head :: tail => insert(head, isort(tail))
//}
//def insert(elem: Int, list: List[Int]): List[Int] = list match {
//  case Nil => List(elem)
//  case head :: tail => if (elem <= head) elem :: list
//  else head :: insert(elem, tail)
//}
