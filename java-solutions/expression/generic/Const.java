package expression.generic;

import expression.generic.evaluator.Evaluator;

import java.util.Objects;

public class Const<T> implements GenericExpression<T> {
    private final String value;

    public Const(T value) {
        this.value = value.toString();
    }

    @Override
    public T evaluate(T x, Evaluator<T> evaluator) {
        return evaluator.parse(value);
    }

    @Override
    public T evaluate(T x, T y, T z, Evaluator<T> evaluator) {
        return this.evaluate(x, evaluator);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Const<T> o = (Const<T>) obj;
        return o.value.equals(this.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value) * 179;
    }
}

