package expression.exceptions;

import expression.MyExpression;

import java.util.Objects;
import java.util.Set;

public class CheckedNegate implements MyExpression {
    private final MyExpression value;

    public CheckedNegate(MyExpression value) {
        this.value = value;
    }

    @Override
    public double evaluate(double x) {
        return -1.0 * value.evaluate(x);
    }

    @Override
    public int evaluate(int x) {
        checkOverflow(value.evaluate(x));
        return -1 * value.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        checkOverflow(value.evaluate(x, y, z));
        return -1 * value.evaluate(x, y, z);
    }

    @Override
    public String toString() {
        return "-(" + value + ")";
    }

    @Override
    public String toMiniString() {
        boolean hasBrackets = value instanceof CheckedAbstractBinary;
        return "-" + (hasBrackets ? "(" : " ") + value.toMiniString() + (hasBrackets ? ")" : "");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        CheckedNegate o = (CheckedNegate) obj;
        return o.value.equals(this.value);
    }
    @Override
    public int hashCode() {
        return Objects.hash(value) * 179;
    }

    protected void checkOverflow(int res) {
        if (res == Integer.MIN_VALUE) {
            throw new OverflowException("UnaryMin");
        }
    }
}
