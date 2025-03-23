package expression.generic;

import expression.generic.evaluator.Evaluator;

public class Divide<T> extends AbstractBinary<T> {
    public Divide(GenericExpression<T> leftPart, GenericExpression<T> rightPart) {
        super(leftPart, rightPart, "/", true);
    }


    @Override
    protected int getType() {
        return 2;
    }

    @Override
    protected T calculate(T x, T y, Evaluator<T> evaluator) {
        return evaluator.divide(x, y);
    }
}

