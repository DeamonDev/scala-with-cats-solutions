package chapter4

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration._

import cats.data.Writer
import cats.instances.vector._
import cats.syntax.applicative._
import cats.syntax.writer._


object ParallelFactorial extends App { 

    type Logged[A] = Writer[Vector[String], A]

    123.pure[Logged]
    println(Vector("msg1", "msg2").tell.run)

    val writer1 = for {
        a <- 10.pure[Logged]
        _ <- Vector("a", "b", "c").tell
        b <- 32.writer(Vector("x", "y", "z"))
    } yield a + b
    println(writer1.run)

    val writer2 = writer1.mapWritten(_.map(_.toUpperCase()))
    println(writer2.run)

    val writer3 = writer1.bimap(
        log => log.map(_.toUpperCase),
        res => res * 1000
    )
    println(writer3.run)

    val writer4 = writer1.mapBoth { (log, res) => 
        val log2 = log.map(_ + "!")
        val res2 = res * 1000
        (log2, res2)
    }
    println(writer4.run)

    def slowly[A](body: => A) = 
        try body finally Thread.sleep(300)

    def factorial(n: Int): Int = { 
        val ans = slowly(if (n == 0) 1 else n * factorial(n - 1))
        println(s"fact $n $ans")
        ans
    }

    Await.result(Future.sequence(Vector(
        Future(factorial(1)),
        Future(factorial(1))
    )), 5.seconds)

    def writerFactorial(n: Int): Logged[Int] =
        for { 
            x <- if (n == 0) 1.pure[Logged] 
                 else slowly(writerFactorial(n - 1).bimap( log => log, ans => ans * n))
            _ <- Vector(s"fact $n $x").tell
        } yield x

    println(writerFactorial(10).run)


    import cats.data.Reader

    final case class Cat(name: String, favoriteFood: String)
    val catName: Reader[Cat, String] = Reader( cat => cat.name )

    println(catName.run(Cat("Omi", "meatty")))

    val greetKitty: Reader[Cat, String] = 
        catName.map( name => s"Hello ${name}")

    println(greetKitty.run(Cat("Omi", "meatty")))

    val feedKitty: Reader[Cat, String] = Reader(cat => s"Have a nice bowl of ${cat.favoriteFood}")

    val greetAndFeed: Reader[Cat, String] = 
        for { 
            greet <- greetKitty
            feed <- feedKitty
        } yield s"$greet. $feed." 

    println(greetAndFeed(Cat("Omi", "meatty")))

}