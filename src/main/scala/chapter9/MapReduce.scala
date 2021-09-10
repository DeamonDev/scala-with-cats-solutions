package chapter9

import cats.Monoid
import cats.syntax.semigroup._
import cats.instances.int._
import cats.instances.string._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._

object MapReduce extends App {
    
    def foldMap[A, B : Monoid](xs: Vector[A])(f: A => B): B = 
        xs.map(f).foldLeft(Monoid[B].empty)(_ |+| _)

    println(foldMap(Vector(1,2,3))(identity))
    println(foldMap(Vector(1,2,3))(_.toString + "!"))
    println(foldMap("Hello world!".toVector)(_.toString.toUpperCase))

    val future1 = Future { 
        (1 to 100).toList.foldLeft(0)(_ + _)
    }

    val future2 = Future { 
        (100 to 200).toList.foldLeft(0)(_ + _)
    }

    val future3 = future1.map(_.toString)

    val future4 = for {
        a <- future1
        b <- future2
    } yield a + b

    println(Future.sequence(List(Future(1), Future(2), Future(3))))
    println(Runtime.getRuntime.availableProcessors)
    //res: Int = 4 

    def parallelFoldMap[A, B : Monoid](xs: Vector[A])(f: A => B): B = {
        val nCores = Runtime.getRuntime.availableProcessors
        val batchSize = Math.ceil(1.0 * xs.length / nCores).toInt

        val groups: Iterator[Vector[A]] = xs.grouped(batchSize)

        val futureValues = groups.map(group => Future {
            group.map(f).foldLeft(Monoid[B].empty)(_ |+| _)
        })

        val result = Future.sequence(futureValues).map(iter => 
            iter.foldLeft(Monoid[B].empty)(_ |+| _))

        Await.result(result, 1.second)

    }


    val result: Int = parallelFoldMap((1 to 100000).toVector)(identity)
    println(result)


}