package expression;

public interface MyExpression extends ToMiniString, TripleExpression {
    double evaluate(double x);

    int evaluate(int x);
}

