package chapter4

import cats.Id

object MonadicSecretIdentities { 

    def pure[A](value: A): Id[A] = value
    def flatMap[A, B](value: Id[A])(f: A => Id[B]): Id[B] = f(value)
    def map[A, B](value: Id[A])(f: A => B): Id[B] = flatMap(value)(a => pure(f(a)))
    //def map[A, B](value: Id[A])(f: A => B): Id[B] = f(value) !!! map "=" flatMap

}