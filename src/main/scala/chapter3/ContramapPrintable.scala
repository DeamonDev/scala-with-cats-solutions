package chapter3

final case class Box[A](value: A)

object ContramapPrintable { 

    trait Printable[A] { self => 
        def format(value: A): String
        def contrmap[B](func: B => A): Printable[B] = new Printable[B] { 
            def format(value: B): String = 
                self.format(func(value))
        }
    }

    def format[A](value: A)(implicit p: Printable[A]): String = 
        p.format(value)

    implicit val stringPrintable: Printable[String] = new Printable[String] { 
        def format(str: String): String = 
            s"'${str}'"
    }

    implicit val booleanPrintable: Printable[Boolean] = new Printable[Boolean] {
        def format(value: Boolean): String = 
            if (value) "OK" else "NO"
    }

    implicit def boxPrintable[A](implicit p: Printable[A]): Printable[Box[A]] = 
        p.contrmap[Box[A]](_.value)      
        
}