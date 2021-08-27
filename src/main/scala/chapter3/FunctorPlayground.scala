package chapter3

import cats.Functor
import cats.instances.list._
import cats.instances.option._
import cats.syntax.functor._
import chapter3.Codec._
import chapter3.ContramapPrintable._
import chapter3.TreeFunctor._


object FunctorPlayground extends App { 
    val list1 = List(1,2,3)
    val list2 = Functor[List].map(list1)(_ * 2)
    
    val option1 = Option(123)
    val option2 = Functor[Option].map(option1)(_.toString)

    val func = (x: Int) => x + 1 
    //func: Int => Int = <function1> 

    val liftedFunc = Functor[Option].lift(func)
    //liftedFunc: Option[Int] => Option[Int] = cats.Functor$$lambda[...]

    println(liftedFunc(Option(1)))

    def doMath[F[_]](start: F[Int])(implicit functor: Functor[F]): F[Int] = 
        start.map(n => (n + 1)).map(n => n * 2)

    println(doMath(Option(20)))
    println(doMath(List(1,2,3)))

    val myTree: Tree[Int] = Branch(Leaf(1), Branch(Leaf(3), Leaf(42)))
    val newTree = myTree.map(x => x * 2)
    println(newTree)

    println(format("hello"))
    println(format(true))

    println(format(Box("Hello Kitty!")))
    println(format(Box(true)))

    println(encode(123.4))
    println(decode[Double]("123.4"))

    println(encode(Box(123.4)))
    println(decode[Box[Double]]("123.4"))

}