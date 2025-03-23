package expression.generic;

import expression.generic.evaluator.Evaluator;

public class Square<T> implements GenericExpression<T> {

    private final GenericExpression<T> value;

    public Square(GenericExpression<T> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "square(" + value + ")";
    }

    @Override
    public String toMiniString() {
        if (value.toString().charAt(0) == '(') {
            return "square(" + value.toMiniString() + ')';
        } else {
            return "square " + value.toMiniString();
        }
    }

    @Override
    public T evaluate(T x, Evaluator<T> evaluator) {
        return evaluator.square(value.evaluate(x, evaluator));
    }

    @Override
    public T evaluate(T x, T y, T z, Evaluator<T> evaluator) {
        return evaluator.square(value.evaluate(x, y, z, evaluator));
    }
}
