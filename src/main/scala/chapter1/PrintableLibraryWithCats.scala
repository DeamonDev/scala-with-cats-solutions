package chapter1

import cats.Show
import cats.syntax.show._

object PrintableLibraryWithCats extends App { 
    

    implicit val catShow: Show[Cat] =
        new Show[Cat] {
            def show(cat: Cat): String = 
                s"${cat.name} is a ${cat.age} year-old ${cat.color} cat."
        }
    
    val omi = Cat("Ominiuszka", 1, "white & black")
    println(omi.show)


}