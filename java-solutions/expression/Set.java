package expression;


public class Set extends AbstractBinary {
    public Set(MyExpression leftPart, MyExpression rightPart) {
        super(leftPart, rightPart, "set", true);
    }

    @Override
    protected int getType() {
        return 0;
    }

    @Override
    protected int calculate(int x, int y) {
        return x | (1<<y);
    }

    @Override
    protected double calculate(double x, double y) {
        return 0;
    }
}
