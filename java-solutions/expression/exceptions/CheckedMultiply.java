package expression.exceptions;

import expression.AbstractBinary;
import expression.MyExpression;

public class CheckedMultiply extends CheckedAbstractBinary {
    public CheckedMultiply(MyExpression leftPart, MyExpression rightPart) {
        super(leftPart, rightPart, "*", false);
    }


    @Override
    protected int getType() {
        return 3;
    }

    @Override
    protected int calculate(int x, int y) {
        checkOverflow(x, y);
        return x * y;
    }

    @Override
    protected double calculate(double x, double y) {
        return x * y;
    }

    protected void checkOverflow(int x, int y) {
        int res = x * y;
        if (x != 0 && y != 0 && (res / x != y || res / y != x)) {
            throw new OverflowException("Multiply");
        }
    }
}

