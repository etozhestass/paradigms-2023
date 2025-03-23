package expression.generic;

import expression.generic.evaluator.Evaluator;

import java.util.Objects;

public class UnaryMin<T> implements GenericExpression<T> {
    private final GenericExpression<T> value;

    public UnaryMin(GenericExpression<T> value) {
        this.value = value;
    }

    @Override
    public T evaluate(T x, Evaluator<T> evaluator) {
        return evaluator.negate(value.evaluate(x, evaluator));
    }

    @Override
    public T evaluate(T x, T y, T z, Evaluator<T> evaluator) {
        return evaluator.negate(value.evaluate(x, y, z, evaluator));
    }

    @Override
    public String toString() {
        return "-(" + value + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UnaryMin) {
            return obj.hashCode() == this.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value) * 179;
    }
}
