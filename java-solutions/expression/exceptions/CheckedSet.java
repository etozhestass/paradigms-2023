package expression.exceptions;

import expression.MyExpression;

public class CheckedSet extends CheckedAbstractBinary {
    public CheckedSet(MyExpression leftPart, MyExpression rightPart) {
        super(leftPart, rightPart, "set", true);
    }

    @Override
    protected int getType() {
        return 1;
    }

    @Override
    protected int calculate(int x, int y) {
        return x | (1 << y);
    }

    @Override
    protected double calculate(double x, double y) {
        return 0;
    }
}
