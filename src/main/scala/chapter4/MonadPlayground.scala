package chapter4

import cats.Monad
import cats.Id
import cats.instances.option._
import cats.instances.list._
import cats.syntax.applicative._ //for pure
import cats.syntax.functor._
import cats.syntax.flatMap._

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


}