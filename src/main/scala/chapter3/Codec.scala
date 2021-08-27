package chapter3


object Codec { 
    
    trait Codec[A] { self => 
        def encode(value: A): String
        def decode(str: String): A 

        def imap[B](dec: A => B, enc: B => A): Codec[B] = new Codec[B] { 
            override def encode(value: B): String = self.encode(enc(value))
            override def decode(str: String): B = dec(self.decode(str))
        }
    }

    def encode[A](value: A)(implicit c: Codec[A]): String = 
        c.encode(value)

    def decode[A](str: String)(implicit c: Codec[A]): A = 
        c.decode(str)

    implicit val stringCodec: Codec[String] = 
        new Codec[String] { 
            def encode(value: String): String = value
            def decode(str: String): String = str
        }

    implicit val intCodec: Codec[Int] = 
        stringCodec.imap(_.toInt, _.toString)

    implicit val booleanCodec: Codec[Boolean] = 
        stringCodec.imap(_.toBoolean, _.toString)

    implicit val doubleCodec: Codec[Double] =
        stringCodec.imap(_.toDouble, _.toString)

    implicit def boxCodec[A](implicit c: Codec[A]): Codec[Box[A]] = 
        c.imap(a => Box(a), _.value)

}