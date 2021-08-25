package chapter2

import cats._
import cats.implicits._ 
//import cats.instances.option


object MonoidPlayground extends App { 

    import chapter2.SupperAdder._

    val combinedInt = Monoid[Int].combine(10, 32)
    val combinedString = Monoid[String].combine("Hello", "Kitty")
    val combinedOption = Monoid[Option[Int]].combine(Option(10), Option(32))

    val stringResult = "Hi " |+| "there" |+| Monoid[String].empty
    val intResult = 1 |+| 2 |+| 3 |+| Monoid[Int].empty
    
    println(combinedInt)
    println(combinedString)
    println(combinedOption)
    println(stringResult)
    println(intResult)

    val intList: List[Int] = List(1,2,3,4)
    println(add(intList))

    val optionIntList: List[Option[Int]] = intList.map(x => Option(x))
    println(add(optionIntList))

    val map1 = Map("a" -> 1, "b" -> 2)
    val map2 = Map("b" -> 3, "d" -> 4)

    println(map1 |+| map2)
    

}