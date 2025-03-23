package expression.generic;

import expression.exceptions.OverflowException;
import expression.generic.evaluator.*;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private static final Map<String, Evaluator<?>> EVALUATOR_MAP = Map.of(
            "bi", new BigIntegerEvaluator(),
            "d", new DoubleEvaluator(),
            "i", new IntegerEvaluator(true),
            "u", new IntegerEvaluator(false),
            "l", new LongEvaluator(),
            "s", new ShortEvaluator()
    );
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        return createTabulate(EVALUATOR_MAP.get(mode), expression, x1, x2, y1, y2, z1, z2);
    }

    private <T> Object[][][] createTabulate(Evaluator<T> evaluator, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        ExpressionParser<T> parser = new ExpressionParser<>(evaluator);
        GenericExpression<T> genericExpression = parser.parse(expression);
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    try {
                        result[i - x1][j - y1][k - z1] = genericExpression.evaluate(evaluator.intToType(i), evaluator.intToType(j), evaluator.intToType(k), evaluator);
                    } catch (ArithmeticException | OverflowException ignored) {
                        result[i - x1][j - y1][k - z1] = null;
                    }
                }
            }
        }
        return result;
    }
}
