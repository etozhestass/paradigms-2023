package expression.generic;

import expression.generic.evaluator.Evaluator;

public class Abs<T> implements GenericExpression<T> {
    private final GenericExpression<T> value;

    public Abs(GenericExpression<T> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "abs(" + value + ")";
    }

    @Override
    public String toMiniString() {
        if (value.toString().charAt(0) == '(') {
            return "abs(" + value.toMiniString() + ')';
        } else {
            return "abs " + value.toMiniString();
        }
    }

    @Override
    public T evaluate(T x, T y, T z, Evaluator<T> evaluator) {
        return evaluator.abs(value.evaluate(x, y, z, evaluator));
    }

    @Override
    public T evaluate(T x, Evaluator<T> evaluator) {
        return evaluator.abs(value.evaluate(x, evaluator));
    }

}
