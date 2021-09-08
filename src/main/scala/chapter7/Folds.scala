package chapter7

object Folds extends App { 
    val myList = (1 to 10).toList
    
    val leftFolded = myList.foldLeft(List.empty[Int])((acc, elem) => elem :: acc)
    println(leftFolded)

    val rightFolded = myList.foldRight(List.empty[Int])((elem, acc) => elem :: acc)
    println(rightFolded)

    /**
      * Implementing map, flatMap, filter and sum using foldRight method
      */
    def foldRightMap[A, B](xs: List[A])(f: A => B): List[B] = 
        xs.foldRight(List.empty[B])((a, acc) => f(a) :: acc)

    println(foldRightMap(myList)(x => x * 10))
    
    def foldRightFlatMap[A, B](xs: List[A])(f: A => List[B]): List[B] = 
        xs.foldRight(List.empty[B])((a, acc) => f(a) ::: acc)

    println(foldRightFlatMap(myList)(x => List(x, x * 10)))

    def foldRightFilter[A](xs: List[A])(f: A => Boolean): List[A] = 
        xs.foldRight(List.empty[A])((a, acc) => if (f(a)) a :: acc else acc)
    
    println(foldRightFilter(myList)(x => x % 2 == 0))

    import cats.Foldable
    import cats.instances.list._
    import cats.instances.option._

    val ints = List(1, 2, 3)

    Foldable[List].foldLeft(ints, 0)(_ + _)

    val maybeInt = Option(123)

    Foldable[Option].foldLeft(maybeInt, 10)(_ * _)

    

}