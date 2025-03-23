package expression.exceptions;

import expression.MyExpression;

public class CheckedClear extends CheckedAbstractBinary {
    public CheckedClear(MyExpression leftPart, MyExpression rightPart) {
        super(leftPart, rightPart, "clear", true);
    }

    @Override
    protected int getType() {
        return 1;
    }

    @Override
    protected int calculate(int x, int y) {
        return x & ~(1 << y);
    }

    @Override
    protected double calculate(double x, double y) {
        return 0;
    }
}
