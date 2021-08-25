package chapter1

object CatEq extends App { 

    import cats.Eq
    import cats.syntax.eq._
    import cats.instances.int._
    import cats.instances.string._
    import cats.instances.option._

    implicit val catEq: Eq[Cat] = Eq.instance[Cat] {
        (cat1, cat2) => (cat1.name === cat2.name) && (cat1.age === cat2.age) && 
                        (cat1.color === cat2.color) 
    }

    val cat1 = Cat("Garfield", 38, "orange & black")
    val cat2 = Cat("Heathcliff", 33, "orange & black")

    val optionCat1 = Option(cat1)
    val optionCat2 = Option(cat2)

    println(cat1 === cat1)
    println(cat1 === cat2)
    println(cat2 === cat2)

    println(optionCat1 === optionCat1)
    println(optionCat1 === optionCat2)
    println(optionCat2 === optionCat2)
}