package expression.generic.evaluator;

public class ShortEvaluator implements Evaluator<Short> {
    @Override
    public Short add(Short left, Short right) {
        return (short) (left + right);
    }

    @Override
    public Short subtract(Short left, Short right) {
        return (short) (left - right);
    }

    @Override
    public Short multiply(Short left, Short right) {
        return (short) (left * right);
    }

    @Override
    public Short divide(Short left, Short right) {
        return (short) (left / right);
    }

    @Override
    public Short negate(Short digit) {
        return (short) (-digit);
    }

    @Override
    public Short abs(Short digit) {
        return (short) Math.abs(digit);
    }

    @Override
    public Short mod(Short left, Short right) {
        return (short) (left % right);
    }

    @Override
    public Short square(Short digit) {
        return (short) (digit * digit);
    }

    @Override
    public Short parse(String expression) {
        return Short.parseShort(expression);
    }

    @Override
    public Short intToType(int digit) {
        return (short) digit;
    }
}
