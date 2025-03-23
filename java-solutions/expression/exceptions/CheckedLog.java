package expression.exceptions;

import expression.MyExpression;

public class CheckedLog implements MyExpression {

    private final MyExpression value;

    public CheckedLog(MyExpression value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return "log10(" + value + ")";
    }

    @Override
    public String toMiniString() {
        if (value.toString().charAt(0) == '(') {
            return "log10(" + value.toMiniString() + ')';
        } else {
            return "log10 " + value.toMiniString();
        }
    }

    private void isPositive(int x) {
        if (x <= 0) {
            throw new NegateNumberLog("The number less than zero: " + x);
        }
    }

    // :NOTE: копипаста в evaluate при вычислении логарифма
    @Override
    public int evaluate(int x, int y, int z) {
        int num = value.evaluate(x, y, z);
        isPositive(num);
        int count = 0;
        while (num >= 10) {
            num /= 10;
            count++;
        }
        return count;
    }

    @Override
    public int evaluate(int x) {
        int num = value.evaluate(x);
        int count = 0;
        while (num > 10) {
            num /= 10;
            count++;
        }
        return count;
    }

    @Override
    public double evaluate(double x) {
        return 0;
    }
}
