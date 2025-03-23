package expression.exceptions;

import expression.MyExpression;

public class CheckedPow implements MyExpression {
    private final MyExpression value;

    public CheckedPow(MyExpression value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "pow10(" + value + ")";
    }

    @Override
    public String toMiniString() {
        if (value.toString().charAt(0) == '(') {
            return "pow10(" + value.toMiniString() + ')';
        } else {
            return "pow10 " + value.toMiniString();
        }
    }

    private void isPositive(int x) {
        if (x < 0) {
            throw new NegateNumberLog("The number less than zero: " + x);
        }
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int num = value.evaluate(x, y, z);
        isPositive(num);
        checkOverflow(num);
        int res = 1;
        for (int i = 0; i < num; i++) {
            res *= 10;
        }
        return res;
    }

    @Override
    public double evaluate(double x) {
        return 0;
    }

    @Override
    public int evaluate(int x) {
        return 0;
    }

    private void checkOverflow(int x) {
        if (x > 9) {
            throw new OverflowException("");
        }
    }
}
