package chapter8

import scala.concurrent.Future
import cats.instances.future._ // for Applicative
import cats.instances.list._   // for Traverse
import cats.syntax.traverse._
import scala.concurrent.ExecutionContext.Implicits.global

trait UptimeClient { 
    def getUptime(hostname: String): Future[Int]
}

class UptimeService(client: UptimeClient) { 
    def getTotalUptime(hostnames: List[String]): Future[Int] = 
        hostnames.traverse(client.getUptime).map(_.sum)
}

class TestUptimeClient(hosts: Map[String, Int]) extends UptimeClient { 
    def getUptime(hostname: String): Future[Int] = 
        Future.successful(hosts.getOrElse(hostname, 0))
}

object TestingCodeWrong extends App { 

    val hosts = Map("host1" -> 10, "host2" -> 6)
    val client = new TestUptimeClient(hosts)
    val service = new UptimeService(client)
    val actual = service.getTotalUptime(hosts.keys.toList)
    val expected = hosts.values.sum
    //assert(actual == expected)

    

}