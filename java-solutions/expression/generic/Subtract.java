package expression.generic;

import expression.generic.evaluator.Evaluator;

public class Subtract<T> extends AbstractBinary<T> {
    public Subtract(GenericExpression<T> leftPart, GenericExpression<T> rightPart) {
        super(leftPart, rightPart, "-", true);
    }


    @Override
    protected int getType() {
        return 1;
    }

    @Override
    protected T calculate(T x, T y, Evaluator<T> evaluator) {
        return evaluator.subtract(x, y);
    }
}

