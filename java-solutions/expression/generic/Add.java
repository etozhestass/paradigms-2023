package expression.generic;

import expression.generic.evaluator.Evaluator;

public class Add<T> extends AbstractBinary<T> {
    public Add(GenericExpression<T> leftPart, GenericExpression<T> rightPart) {
        super(leftPart, rightPart, "+", false);
    }

    @Override
    protected int getType() {
        return 1;
    }

    @Override
    protected T calculate(T x, T y, Evaluator<T> evaluator) {
        return evaluator.add(x, y);
    }
}

