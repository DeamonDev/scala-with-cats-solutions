package chapter7

import scala.concurrent.Future
import scala.concurrent.Await

object Folds extends App { 
    val myList = (1 to 10).toList
    
    val leftFolded = myList.foldLeft(List.empty[Int])((acc, elem) => elem :: acc)
    println(leftFolded)

    val rightFolded = myList.foldRight(List.empty[Int])((elem, acc) => elem :: acc)
    println(rightFolded)

    /**
      * Implementing map, flatMap, filter and sum using foldRight method
      */
    def foldRightMap[A, B](xs: List[A])(f: A => B): List[B] = 
        xs.foldRight(List.empty[B])((a, acc) => f(a) :: acc)

    println(foldRightMap(myList)(x => x * 10))
    
    def foldRightFlatMap[A, B](xs: List[A])(f: A => List[B]): List[B] = 
        xs.foldRight(List.empty[B])((a, acc) => f(a) ::: acc)

    println(foldRightFlatMap(myList)(x => List(x, x * 10)))

    def foldRightFilter[A](xs: List[A])(f: A => Boolean): List[A] = 
        xs.foldRight(List.empty[A])((a, acc) => if (f(a)) a :: acc else acc)
    
    println(foldRightFilter(myList)(x => x % 2 == 0))

    import cats.Foldable
    import cats.instances.list._
    import cats.instances.option._

    val ints = List(1, 2, 3)

    Foldable[List].foldLeft(ints, 0)(_ + _)

    val maybeInt = Option(123)

    Foldable[Option].foldLeft(maybeInt, 10)(_ * _)

    import cats.Traverse
    import cats.instances.future._
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._

    val hostnames = List(
      "alpha.example.com",
      "beta.example.com",
      "gamma.demo.com"
    )
    def getUptime(hostname: String): Future[Int] =
      Future(hostname.length * 60)

    val totalUpTime: Future[List[Int]] = 
        Traverse[List].traverse(hostnames)(getUptime)

    println(Await.result(totalUpTime, 1.second))

    val numbers = List(Future(1), Future(2), Future(3))
    val numbers2: Future[List[Int]] = Traverse[List].sequence(numbers)

    println(Await.result(numbers2, 1.second))

    

}