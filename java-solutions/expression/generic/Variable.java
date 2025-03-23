package expression.generic;

import expression.exceptions.UnexpectedTokenException;
import expression.generic.evaluator.Evaluator;

import java.util.Objects;

public class Variable<T> implements GenericExpression<T> {
    private final String value;

    public Variable(String value) {
        this.value = value;
    }


    @Override
    public T evaluate(T x, Evaluator<T> evaluator) {
        if ("x".equals(value)) {
            return x;
        }
        throw new UnexpectedTokenException("No such variable");
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public T evaluate(T x, T y, T z, Evaluator<T> evaluator) {
        return switch(value) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new UnexpectedTokenException("No such variable");
        };
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable) {
            return obj.hashCode() == this.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value) * 179;
    }
}