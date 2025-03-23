package expression.generic.evaluator;

public class LongEvaluator implements Evaluator<Long> {
    @Override
    public Long add(Long left, Long right) {
        return left + right;
    }

    @Override
    public Long subtract(Long left, Long right) {
        return left - right;
    }

    @Override
    public Long multiply(Long left, Long right) {
        return left * right;
    }

    @Override
    public Long divide(Long left, Long right) {
        return left / right;
    }

    @Override
    public Long negate(Long digit) {
        return -digit;
    }

    @Override
    public Long abs(Long digit) {
        return Math.abs(digit);
    }

    @Override
    public Long mod(Long left, Long right) {
        return left % right;
    }

    @Override
    public Long square(Long digit) {
        return digit * digit;
    }

    @Override
    public Long parse(String expression) {
        return Long.parseLong(expression);
    }

    @Override
    public Long intToType(int digit) {
        return (long) digit;
    }
}
