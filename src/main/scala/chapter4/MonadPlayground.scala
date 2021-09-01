package chapter4

import cats.Monad
import cats.Id
import cats.instances.option._
import cats.instances.list._
import cats.syntax.applicative._ //for pure
import cats.syntax.functor._
import cats.syntax.flatMap._
import cats.syntax.either._

object MonadPlayground extends App { 

    val opt1 = Monad[Option].pure(3)
    val opt2 = Monad[Option].flatMap(opt1)(a => Some(a + 2))
    val opt3 = Monad[Option].map(opt2)(a => 100 * a)
    println(opt3)
    
    val list1 = Monad[List].pure(3)
    val list2 = Monad[List].flatMap(List(1,2,3))(a => List(a, a*10))
    val list3 = Monad[List].map(list2)(a => a + 123)
    println(list3)

    //syntax
    println(1.pure[Option])
    println(1.pure[List])

    def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] = 
        a.flatMap(x => b.map(y => x*x + y*y))

    /*
        def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] = 
            for {
                x <- a
                y <- b
            } yield x*x + y*y
    */

    println(sumSquare(Option(3), Option(4)))
    println(sumSquare(List(1,2,3), List(4,5)))
    println(sumSquare(3: Id[Int], 4: Id[Int]))

    val a = 3.asRight[String]
    val b = 4.asRight[String]

    for { 
        x <- a 
        y <- b
    } yield x*x + y*y

    def countPositive(nums: List[Int]) = nums.foldLeft(0.asRight[String]) { 
        (acc, num) => if (num > 0) acc.map(_ + 1) else Left("Negative, stopping")
    }

    println(countPositive(List(1,2,3)))
    println(countPositive(List(1,-2,3)))

    //database playground
    val users = Map(
        1 -> "piotr",
        2 -> "maciej",
        3 -> "bodzio"
    )

    val passwords = Map(
        "piotr" -> "kitty",
        "maciej" -> "acidburn",
        "bodzio" -> "brewiarz"
    )

    val db = Db(users, passwords)
    
    println(chapter4.DatabaseWithReader.checkLogin(1, "kitty").run(db))
    println(chapter4.DatabaseWithReader.checkLogin(4, "davinci").run(db))

}