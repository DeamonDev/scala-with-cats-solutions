package chapter1

import chapter1.JSONWriter._

final case class Person(name: String, email: String) 

object JSONWriterInstances { 
    implicit val stringWriter: JSONWriter[String] = 
        new JSONWriter[String] {
            def write(value: String): JSON = 
                JSONString(value)
        }

    implicit val personWriter: JSONWriter[Person] = 
        new JSONWriter[Person] { 
            def write(person: Person): JSON =
                JSONObject(Map(
                    "name" -> JSONString(person.name),
                    "email" -> JSONString(person.email)
                ))
        }

    implicit def optionWriter[A](implicit writer: JSONWriter[A]): JSONWriter[Option[A]] =
        new JSONWriter[Option[A]] {
            def write(option: Option[A]): JSON = 
                option match {
                    case Some(aValue) => writer.write(aValue)
                    case None         => JSNull
                }
        }
    
}