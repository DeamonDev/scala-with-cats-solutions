package chapter8

import cats.instances.list._
import cats.syntax.functor._
import cats.syntax.traverse._
import cats.{Applicative, Id}

import scala.concurrent.Future

object TestingCode extends App { 

    trait UptimeClient[F[_]] {
        def getUptime(hostname: String): F[Int]
    }

    trait RealUptimeClient extends UptimeClient[Future] { 
        def getUptime(hostname: String): Future[Int] 
    }

    trait JerkUptimeClient extends UptimeClient[Id] { 
        def getUptime(hostname: String): Id[Int] 
    }

    class TestUptimeClient(hosts: Map[String, Int]) extends JerkUptimeClient { 
        override def getUptime(hostname: String): Id[Int] = 
            hosts.getOrElse(hostname, 0) // OK, because Id is specific...

    }

    class UptimeService[F[_] : Applicative](client: UptimeClient[F]) { 
        def getTotalUptime(hostnames: List[String]): F[Int] = 
            hostnames.traverse(client.getUptime).map(_.sum)
    }

    val hosts = Map("host1" -> 10, "host2" -> 6)
    val client = new TestUptimeClient(hosts)
    val service = new UptimeService(client)
    val actual = service.getTotalUptime(hosts.keys.toList)
    val expected = hosts.values.sum
    println(assert(actual == expected))

}