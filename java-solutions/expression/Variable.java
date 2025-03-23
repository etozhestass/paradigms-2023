package expression;

import java.util.Objects;

public class Variable implements MyExpression {
    private final String value;

    public Variable(String value) {
        this.value = value;
    }

    @Override
    public double evaluate(double x) {
        if ("x".equals(value)) {
            return x;
        }
        return 0;
    }

    @Override
    public int evaluate(int x) {
        if ("x".equals(value)) {
            return x;
        }
        return 0;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch(value) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> 0;
        };
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Variable o = (Variable) obj;
        return value.equals(o.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value) * 179;
    }
}