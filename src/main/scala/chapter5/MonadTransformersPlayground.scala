package chapter5

import cats.data.OptionT
import cats.instances.list._
import cats.syntax.applicative._
import cats.instances.either._
import cats.data.EitherT
import scala.concurrent.Future

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

    //usage patterns
    /**
      * 1 --> super stack
      */
    sealed abstract class HttpError
    final case class NotFound(item: String) extends HttpError
    final case class BadRequest(msg: String) extends HttpError

    type FutureEither[A] = EitherT[Future, HttpError, A]

    /**
      * 2 --> gluing
      */

    import cats.data.Writer

    type Logged[A] = Writer[List[String], A]

    def parseNumber(str: String): Logged[Option[Int]] = 
        util.Try(str.toInt).toOption match {
            case Some(num) => Writer(List(s"Read $str"), Some(num))
            case None      => Writer(List(s"Failed on $str"), None)
        }

    def addAll(a: String, b: String, c: String): Logged[Option[Int]] = {

        val result = for {
            x <- OptionT(parseNumber(a))
            y <- OptionT(parseNumber(b))
            z <- OptionT(parseNumber(c))
        } yield x + y + z

        result.value
    }

    val sum1 = addAll("10", "16", "49")
    println(sum1)

    val sum2 = addAll("10", "a", "49")
    println(sum2)

    




    

}