package chapter2

import cats._

case class Order(totalCost: Double, quantity: Double)

object SupperAdder {

    implicit val orderMonoid: Monoid[Order] = new Monoid[Order] { 
        override def combine(x: Order, y: Order): Order = Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
        override def empty: Order = Order(0,0)
    }
    
    implicit val intMonoid: Monoid[Int] = new Monoid[Int] { 
        override def combine(x: Int, y: Int): Int = x + y
        override def empty: Int = 0
    }

    implicit val optionMonoid: Monoid[Option[Int]] = new Monoid[Option[Int]] { 
        override def combine(x: Option[Int], y: Option[Int]): Option[Int] = (x, y) match { 
            case (Some(a), Some(b)) => Some(intMonoid.combine(a, b))
            case (_, None) => None
            case (None, _) => None
        }

        override def empty: Option[Int] =  Some(intMonoid.empty) 
    }
    def add[A](xs: List[A])(implicit m: Monoid[A]): A = 
        xs.foldRight[A](m.empty)( (h, acc) => m.combine(h, acc))


}