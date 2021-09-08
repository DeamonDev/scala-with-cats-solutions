package chapter5

import scala.concurrent.Future
import cats.data.EitherT
import scala.concurrent.ExecutionContext.Implicits.global
import cats.instances.future._
import scala.concurrent.duration._
import scala.concurrent.Await

object TransformAndRollOut extends App { 
    type Response[A] = EitherT[Future, String, A]

    val powerLevels = Map(
        "Jazz" -> 6,
        "Bumblebee" -> 8,
        "Hot Rod" -> 10
    )

    def getPowerLevel(autobot: String): Response[Int] = 
        powerLevels.contains(autobot) match { 
            case true => EitherT.right(Future(powerLevels(autobot)))
            case false => EitherT.left(Future(s"autobot: $autobot is not reachable"))

        }

    println(getPowerLevel("Hot Rod"))

    def canSpecialMove(ally1: String, ally2: String): Response[Boolean] = 
        for { 
            powerLevel1 <- getPowerLevel(ally1)
            powerLevel2 <- getPowerLevel(ally2)
        } yield (powerLevel1 + powerLevel2) > 15

    println(canSpecialMove("Jazz", "Hot Rod"))

    def tacticalReport(ally1: String, ally2: String): String = {
        val stack = canSpecialMove(ally1, ally2).value

        Await.result(stack, 1.second) match { 
            case Left(msg) => s"Comms error: $msg"
            case Right(true) => s"$ally1 and $ally2 are ready to roll out!"
            case Right(false) => s"$ally1 and $ally2 needs a recharge."
        }
    }

    println(tacticalReport("Jazz", "Bumblebee"))
    println(tacticalReport("Jazz", "Hot Rod"))
    println(tacticalReport("Jazz", "Omi the cat"))


    
}