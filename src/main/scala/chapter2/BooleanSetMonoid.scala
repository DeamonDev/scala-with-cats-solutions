package chapter2

import scala.collection.mutable.Set

object BooleanSetMonoid extends App { 

    trait Semigroup[A] { 
        def combine(x: A, y: A): A 
    }

    trait Monoid[A] extends Semigroup[A] {
       def empty: A
    }

    def apply[A](implicit monoid: Monoid[A]) = 
        monoid

    val orMonoid: Monoid[Boolean] = new Monoid[Boolean] { 
        override def combine(x: Boolean, y: Boolean): Boolean = x || y
        override def empty: Boolean = false
    }

    val andMonoid: Monoid[Boolean] = new Monoid[Boolean] {
        override def combine(x: Boolean, y: Boolean): Boolean = x && y
        override def empty: Boolean = true
    }

    def unionMonoid[A]: Monoid[Set[A]] = new Monoid[Set[A]] { 
        override def combine(x: Set[A], y: Set[A]): Set[A] = x union y
        override def empty: Set[A] = Set.empty[A] 
    }

    

    println(orMonoid.combine(false, true))
    println(andMonoid.combine(true, false))
}