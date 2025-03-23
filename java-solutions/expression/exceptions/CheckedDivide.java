package expression.exceptions;

import expression.AbstractBinary;
import expression.MyExpression;

public class CheckedDivide extends CheckedAbstractBinary {
    public CheckedDivide(MyExpression leftPart, MyExpression rightPart) {
        super(leftPart, rightPart, "/", true);
    }


    @Override
    protected int getType() {
        return 3;
    }

    @Override
    protected int calculate(int x, int y) {
        checkDivideByZero(y);
        checkOverflow(x, y);
        return x / y;
    }

    @Override
    protected double calculate(double x, double y) {
        return x / y;
    }

    protected void checkDivideByZero(int y) {
        if (y == 0) {
            throw new DivideByZeroException();
        }
    }
    protected void checkOverflow(int x, int y) {
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException("Divide");
        }
    }
}

