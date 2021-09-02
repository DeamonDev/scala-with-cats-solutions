package chapter4

import cats.Monad

sealed trait Tree[+A] 

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

object Tree {
    def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = 
        Branch(left, right)

    def leaf[A](value: A): Tree[A] =
        Leaf(value)

    implicit val treeMonad = new Monad[Tree] { 
        def pure[A](value: A): Tree[A] = leaf(value)
        def flatMap[A, B](ta: Tree[A])(f: A => Tree[B]): Tree[B] = ta match { 
            case Branch(l, r) => Branch(flatMap(l)(f), flatMap(r)(f))
            case Leaf(a)      => f(a)
        }
        def tailRecM[A, B](a: A)(f: A => Tree[Either[A,B]]): Tree[B] = ???
    }

        
    
}