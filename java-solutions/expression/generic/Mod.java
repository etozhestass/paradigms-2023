package expression.generic;

import expression.generic.evaluator.Evaluator;

public class Mod<T> extends AbstractBinary<T> {

    public Mod(GenericExpression<T> leftPart, GenericExpression<T> rightPart) {
        super(leftPart, rightPart, "mod", true);
    }

    @Override
    protected int getType() {
        return 1;
    }

    @Override
    protected T calculate(T x, T y, Evaluator<T> evaluator) {
        return evaluator.mod(x, y);
    }
}
