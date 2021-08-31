package chapter4

import cats.Eval

object FoldRightEval {

    def foldRightEval[A, B](xs: List[A], acc: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = xs match { 
        case head :: tail => Eval.defer(f(head, foldRightEval(tail, acc)(f)))
        case Nil          => acc
    }


}