package chapter5

import cats.data.OptionT
import cats.instances.list._
import cats.syntax.applicative._
import cats.instances.either._

object MonadTransformersPlayground extends App {

    type ListOption[A] = OptionT[List, A]

    val result1: ListOption[Int] = OptionT(List(Option(10)))
    println(result1)

    val result2: ListOption[Int] = 32.pure[ListOption]
    println(result2)

    val result3 = 
    result1.flatMap { (x: Int) => 
        result2.map { (y: Int) => 
            x + y
        }
    }
    println(result3)

    //more advanced type aliasing
    type ErrorOr[A] = Either[String, A]
    type ErrorOrOption[A] = OptionT[ErrorOr, A]

    val a = 10.pure[ErrorOrOption]



    

}