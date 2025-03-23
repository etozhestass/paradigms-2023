package expression.generic;

import expression.generic.evaluator.Evaluator;

public class Multiply<T> extends AbstractBinary<T> {
    public Multiply(GenericExpression<T> leftPart, GenericExpression<T> rightPart) {
        super(leftPart, rightPart, "*", false);
    }


    @Override
    protected int getType() {
        return 2;
    }

    @Override
    protected T calculate(T x, T y, Evaluator<T> evaluator) {
        return evaluator.multiply(x, y);
    }
}

