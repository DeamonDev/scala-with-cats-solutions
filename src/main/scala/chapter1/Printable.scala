package chapter1


object Printable { 

    def format[A](a: A)(implicit printable: Printable[A]): String = printable.format(a)
    def print[A](a: A)(implicit printable: Printable[A]): Unit = println(format(a))

    trait Printable[A] { 
        def format(a: A): String
    }
}