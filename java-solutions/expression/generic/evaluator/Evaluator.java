package expression.generic.evaluator;

public interface Evaluator<T> {
    T add(T left, T right);
    T subtract(T left, T right);
    T multiply(T left, T right);
    T divide(T left, T right);
    T negate(T digit);
    T abs(T digit);
    T mod(T left, T right);
    T square(T digit);
    T parse(String toString);
    T intToType(int digit);
}
