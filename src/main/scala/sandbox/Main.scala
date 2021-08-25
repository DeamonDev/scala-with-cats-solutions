package sandbox

import cats._
import java.util.Date
import cats.Eq

object Main extends App {

  import cats.implicits._

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
      Show.show(date => s"{date.getTime}ms since the epoch.")
  */


  implicit val dateEq: Eq[Date] = Eq.instance[Date] { 
    (date1, date2) => date1.getTime === date2.getTime
  }

  val x = new Date()
  val y = new Date() 

  println(x === x)
  println(x === y)

}
