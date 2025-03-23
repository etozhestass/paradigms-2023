package expression.exceptions;

import expression.MyExpression;

import java.util.Objects;

public abstract class CheckedAbstractBinary implements MyExpression {
    protected final MyExpression leftPart;
    protected final MyExpression rightPart;
    private final String operator;
    private final boolean priority;
    protected abstract int getType();
    protected abstract int calculate(int x, int y);
    protected abstract double calculate(double x, double y);

    public CheckedAbstractBinary(MyExpression leftPart, MyExpression rightPart, String operator, boolean priority) {
        this.leftPart = leftPart;
        this.rightPart = rightPart;
        this.operator = operator;
        this.priority = priority;
    }

    @Override
    public double evaluate(double x) {
        return calculate(leftPart.evaluate(x), rightPart.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return calculate(leftPart.evaluate(x, y, z), rightPart.evaluate(x, y, z));
    }

    @Override
    public int evaluate(int x) {
        return calculate(leftPart.evaluate(x), rightPart.evaluate(x));
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", leftPart.toString(), operator, rightPart.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        final CheckedAbstractBinary o = (CheckedAbstractBinary) obj;
        return leftPart.equals(o.leftPart) && rightPart.equals(o.rightPart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftPart, rightPart, operator, priority) * 179;
    }

    private boolean less(MyExpression obj) {
        if (!(obj instanceof CheckedAbstractBinary)) {
            return false;
        }
        CheckedAbstractBinary o = (CheckedAbstractBinary) obj;
        return o.getType() < this.getType();
    }

    @Override
    public String toMiniString() {
        return String.format("%s %s %s",
                toMiniString(leftPart, less(leftPart)),
                operator,
                toMiniString(rightPart, less(rightPart) || right(rightPart))
        );
    }

    private String toMiniString(MyExpression obj, boolean check) {
        String parsed = "%s";
        if (check) {
            parsed = String.format("(%s)", parsed);
        }
        return String.format(parsed, obj.toMiniString());
    }

    private boolean right(MyExpression obj) {
        if (!(obj instanceof CheckedAbstractBinary)) {
            return false;
        }
        CheckedAbstractBinary o = (CheckedAbstractBinary) obj;
        if (priority && this.getType() >= o.getType()) {
            return true;
        }
        // :NOTE: если добавят новую операцию придётся менять код, так не надо :(
        return operator.equals("*") && o.priority;
    }
}

