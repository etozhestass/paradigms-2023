package expression.generic;

import expression.ToMiniString;
import expression.TripleExpression;
import expression.generic.evaluator.Evaluator;

public interface GenericExpression<T> extends ToMiniString {
    T evaluate(T x, Evaluator<T> evaluator);
    T evaluate(T x, T y, T z, Evaluator<T> evaluator);
}

