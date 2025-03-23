package expression.exceptions;

import expression.AbstractBinary;
import expression.MyExpression;

public class CheckedSubtract extends CheckedAbstractBinary {
    public CheckedSubtract(MyExpression leftPart, MyExpression rightPart) {
        super(leftPart, rightPart, "-", true);
    }


    @Override
    protected int getType() {
        return 2;
    }

    @Override
    protected int calculate(int x, int y) {
        checkOverflow(x, y);
        return x - y;
    }

    private void checkOverflow(int x, int y) {
        if ((y < 0 && Integer.MAX_VALUE + y < x) || (y > 0 && Integer.MIN_VALUE + y > x)) {
            throw new OverflowException("Subtract");
        }
    }

    @Override
    protected double calculate(double x, double y) {
        return x - y;
    }
}

