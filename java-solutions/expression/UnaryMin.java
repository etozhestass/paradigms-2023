package expression;

import java.util.Objects;

public class UnaryMin implements MyExpression {
    private final MyExpression value;

    public UnaryMin(MyExpression value) {
        this.value = value;
    }

    @Override
    public double evaluate(double x) {
        return -1.0 * value.evaluate(x);
    }

    @Override
    public int evaluate(int x) {
        return -1 * value.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return -1 * value.evaluate(x, y, z);
    }

    @Override
    public String toString() {
        return "-(" + value + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        UnaryMin o = (UnaryMin) obj;
        return o.value.equals(this.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value) * 179;
    }
}
