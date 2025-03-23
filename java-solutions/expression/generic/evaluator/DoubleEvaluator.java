package expression.generic.evaluator;

public class DoubleEvaluator implements Evaluator<Double> {
    @Override
    public Double add(Double left, Double right) {
        return left + right;
    }

    @Override
    public Double subtract(Double left, Double right) {
        return left - right;
    }

    @Override
    public Double multiply(Double left, Double right) {
        return left * right;
    }

    @Override
    public Double divide(Double left, Double right) {
        return left / right;
    }

    @Override
    public Double negate(Double digit) {
        return -digit;
    }

    @Override
    public Double abs(Double digit) {
        return Math.abs(digit);
    }

    @Override
    public Double mod(Double left, Double right) {
        return left % right;
    }

    @Override
    public Double square(Double digit) {
        return digit * digit;
    }

    @Override
    public Double parse(String digit) {
        return Double.parseDouble(digit);
    }

    @Override
    public Double intToType(int digit) {
        return (double) digit;
    }

}
