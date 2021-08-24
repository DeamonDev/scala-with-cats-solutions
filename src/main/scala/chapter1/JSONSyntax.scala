package chapter1

import chapter1.JSONWriter._
import chapter1.JSONWriterInstances._

object JSONSyntax extends App { 
    implicit class JSONWriterOps[A](value: A) { 
        def toJSON(implicit writer: JSONWriter[A]): JSON =
            writer.write(value)
    }

    val personToJSON2 = Person("Piotr2", "deamondev2@gmail.com").toJSON
    println(personToJSON2)
}