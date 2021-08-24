package chapter1

import chapter1.JSONWriter._
import chapter1.JSONWriterInstances._

object JSON extends App {
    def toJSON[A](value: A)(implicit writer: JSONWriter[A]): JSON = 
        writer.write(value)

    val personToJSON = toJSON(Person("Piotr", "deamondev@gmail.com"))
    println(personToJSON)

    val optionStringToJSON = JSON.toJSON(Option[String]("Hello Kitty!"))
    println(optionStringToJSON)
}