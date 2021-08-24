package chapter1

import chapter1.Printable._

final case class Cat(name: String, age: Int, color: String)

object PrintableInstances { 
    implicit val printableInt: Printable[Int] = 
        new Printable[Int] { 
            def format(i: Int): String = s"Integer: $i"
        }

    implicit val printableString: Printable[String] = 
        new Printable[String] { 
            def format(s: String): String = s"String: $s"
        }

    implicit val printableCat: Printable[Cat] = 
        new Printable[Cat] { 
            def format(cat: Cat): String = 
                s"${cat.name} is a ${cat.age} year-old ${cat.color} cat."
        }

    

}