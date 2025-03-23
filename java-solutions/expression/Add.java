package expression;

public class Add extends AbstractBinary {
    public Add(MyExpression leftPart, MyExpression rightPart) {
        super(leftPart, rightPart, "+", false);
    }

    @Override
    protected int getType() {
        return 1;
    }

    @Override
    protected int calculate(int x, int y) {
        return x + y;
    }

    @Override
    protected double calculate(double x, double y) {
        return x + y;
    }
}

