package chapter6

import cats.Semigroupal
import cats.instances.option._
import cats.syntax.apply._
object SemigroupalApplicativePlayground extends App {
    val s1 = Semigroupal[Option].product(Some(123), Some("abc"))
    println(s1)
    val s2 = Semigroupal[Option].product(None, Some("XD"))
    println(s2)

    (Option(123), Option("abc")).tupled


  
}
