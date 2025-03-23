package expression.exceptions;

import expression.MyExpression;

public class CheckedCount implements MyExpression{

    private final MyExpression value;

    public CheckedCount(MyExpression value) {
        this.value = value;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return Integer.bitCount(value.evaluate(x, y, z));
    }

    @Override
    public double evaluate(double x) {
        return 0;
    }

    @Override
    public int evaluate(int x) {
        return Integer.bitCount(value.evaluate(x));
    }

    @Override
    public String toString() {
        return "count(" + value + ')';
    }

    @Override
    public String toMiniString() {
        if (value.toString().charAt(0) == '(') {
            return "count(" + value.toMiniString() + ')';
        } else {
            return "count " + value.toMiniString();
        }
    }
}
