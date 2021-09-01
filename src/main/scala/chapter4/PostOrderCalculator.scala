package chapter4

import cats.data.State
import cats.syntax.applicative._

object PostOrderCalculator extends App { 
    type CalcState[A] = State[List[Int], A]

    def evalOne(sym: String): CalcState[Int] = sym match { 
        case "+" => operator(_ + _)
        case "-" => operator(_ - _)
        case "*" => operator(_ * _)
        case "/" => operator(_ / _)
        case num => operand(num.toInt)
    }

    def evalAll(input: List[String]): CalcState[Int] = 
        input.foldLeft(0.pure[CalcState])( (acc, s) => acc.flatMap(_ => evalOne(s)) )

    def evalInput(input: String): Int = 
        evalAll(input.split(" ").toList).runA(Nil).value

    def operand(value: Int): CalcState[Int] = State[List[Int], Int] {
        stack => (value :: stack, value)
    }

    def operator(f: (Int, Int) => Int): State[List[Int], Int] = 
        State[List[Int], Int] { xs => xs match { 
            case x :: y :: tail => 
                val ans = f(x, y)
                (ans :: tail, ans)

            case _ => sys.error("FAILED!")
        }
    }

    val basicCalculation = for {
        _ <- evalOne("1")
        _ <- evalOne("2")
        ans <- evalOne("+")
    } yield ans 

    println(basicCalculation.runA(Nil).value)

    val multistageCalculation = evalAll(List("1", "2", "+", "3", "*"))
    println(multistageCalculation.runA(Nil).value)

    val ev = evalInput("1 2 + 3 * 10 *")
    println(ev)

}