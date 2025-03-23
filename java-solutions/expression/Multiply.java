package expression;

public class Multiply extends AbstractBinary {
    public Multiply(MyExpression leftPart, MyExpression rightPart) {
        super(leftPart, rightPart, "*", false);
    }


    @Override
    protected int getType() {
        return 2;
    }

    @Override
    protected int calculate(int x, int y) {
        return x * y;
    }

    @Override
    protected double calculate(double x, double y) {
        return x * y;
    }
}

