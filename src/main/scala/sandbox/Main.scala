package sandbox

import cats._
import cats.implicits._
import java.util.Date

object Main extends App {

  val showInt: Show[Int] = Show.apply[Int]
  val showString: Show[String] = Show.apply[String]

  val intAsString: String = showInt.show(42)
  val stringAsString: String = showString.show("Omi the cat!")
  

  implicit val dateShow: Show[Date] = 
    new Show[Date] { 
      def show(date: Date): String = 
        s"${date.getTime}ms since the epoch."
    }

  println(new Date().show)
  // 1629830517073ms since the epoch.
  
  /*
    implicit val dateShow: Show[Date] = 
      Show.show(date => s"{date.getTime}ms since the spoch.")
  */


}
