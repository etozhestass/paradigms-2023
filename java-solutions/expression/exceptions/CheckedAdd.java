package expression.exceptions;

import expression.MyExpression;

public class CheckedAdd extends CheckedAbstractBinary {
    public CheckedAdd(MyExpression leftPart, MyExpression rightPart) {
        super(leftPart, rightPart, "+", false);
    }

    @Override
    protected int getType() {
        return 2;
    }

    @Override
    protected int calculate(int x, int y) {
        checkOverflow(x, y);
        return x + y;
    }

    @Override
    protected double calculate(double x, double y) {
        return x + y;
    }


    private void checkOverflow(int x, int y) {
        if ((y > 0 && Integer.MAX_VALUE - y < x) || (y < 0 && Integer.MIN_VALUE - y > x)) {
            throw new OverflowException("Addition");
        }
    }
}

