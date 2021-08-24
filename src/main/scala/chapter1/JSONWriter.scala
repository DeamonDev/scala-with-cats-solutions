package chapter1

object JSONWriter {

    sealed trait JSON

    final case class JSONObject(get: Map[String, JSON]) extends JSON
    final case class JSONString(get: String) extends JSON
    final case class JSONNumber(get: Double) extends JSON
    final case object JSNull extends JSON

    //This is our type class
    trait JSONWriter[A] { 
        def write(value: A): JSON
    }

}
