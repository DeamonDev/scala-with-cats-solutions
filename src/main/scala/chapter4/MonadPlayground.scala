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

    import cats.data.State
    import State._

    val aState = State[Int, String] { state => (state, s"The state is $state")}

    val (state, result) = aState.run(10).value
    println("(state, result) = " + "(" + state + ", " + result + ")")

    val justTheState = aState.runS(10).value
    println(justTheState)

    val justTheResult = aState.runA(10).value
    println(justTheResult)

    val step1 = State[Int, String] { num => 
        val ans = num + 1
        (ans, s"Result of step1: $ans")
    }

    val step2 = State[Int, String] { num => 
        val ans = num * 2
        (ans, s"Result of step2: $ans")
    }

    val both = for { 
        a <- step1
        b <- step2
    } yield (a,b)

    val (s, r) = both.run(20).value

    val program: State[Int, (Int, Int, Int)] = for { 
        a <- get[Int]
        _ <- set[Int](a + 1)
        b <- get[Int]
        _ <- modify[Int](_ + 1)
        c <- inspect[Int, Int](_ * 1000)
    } yield (a, b, c)

    val (state1, result1) = program.run(1).value
    println("(state, result) = (" + state1 + ", " + result1 + ")")

    import cats.syntax.flatMap._

    def retry[F[_] : Monad, A](start: A)(f: A => F[A]): F[A] = 
        f(start).flatMap { a => 
            retry(a)(f)
        }


    def retryTailRecM[F[_] : Monad, A](start: A)(f: A => F[A]): F[A] = 
        Monad[F].tailRecM(start) { a => 
            f(a).map(a2 => Left(a2))
        }

     println(retryTailRecM(100)(a => if (a == 0) None else Some(a - 1)))
}