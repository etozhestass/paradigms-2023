package expression.generic.evaluator;

import java.math.BigInteger;

public class BigIntegerEvaluator implements Evaluator<BigInteger> {
    @Override
    public BigInteger add(BigInteger left, BigInteger right) {
        return left.add(right);
    }

    @Override
    public BigInteger subtract(BigInteger left, BigInteger right) {
        return left.subtract(right);
    }

    @Override
    public BigInteger multiply(BigInteger left, BigInteger right) {
        return left.multiply(right);
    }

    @Override
    public BigInteger divide(BigInteger left, BigInteger right) {
        return left.divide(right);
    }

    @Override
    public BigInteger negate(BigInteger digit) {
        return digit.negate();
    }

    @Override
    public BigInteger abs(BigInteger digit) {
        return digit.abs();
    }

    @Override
    public BigInteger mod(BigInteger left, BigInteger right) {
        return left.mod(right);
    }

    @Override
    public BigInteger square(BigInteger digit) {
        return digit.multiply(digit);
    }

    @Override
    public BigInteger parse(String digit) {
        return new BigInteger(digit);
    }

    @Override
    public BigInteger intToType(int digit) {
        return new BigInteger(String.valueOf(digit));
    }

}
