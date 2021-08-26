package chapter3

import cats.Functor

sealed trait Tree[+A]

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

object TreeFunctor{

    implicit val treeFunctor: Functor[Tree] = new Functor[Tree] { 
          def map[A,B](tree: Tree[A])(f: A => B): Tree[B] = tree match { 
              case Leaf(a) => Leaf(f(a))
              case Branch(l, r) => Branch(map(l)(f), map(r)(f))
            }     
        
        
     }

     
        

}