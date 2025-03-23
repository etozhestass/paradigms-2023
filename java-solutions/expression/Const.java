package expression;

import java.util.Objects;

public class Const implements MyExpression {
    private final String value;

    public Const(int value) {
        this.value = Integer.toString(value);
    }

    public Const(double value) {
        this.value = Double.toString(value);
    }

    @Override
    public double evaluate(double x) {
        return Double.parseDouble(value);
    }

    @Override
    public int evaluate(int x) {
        return Integer.parseInt(value);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return this.evaluate(x);
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
        Const o = (Const) obj;
        return o.value.equals(this.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value) * 179;
    }
}

