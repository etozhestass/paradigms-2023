package expression.parser;

import expression.*;
import expression.exceptions.TripleParser;

import java.util.List;
import java.util.Map;

public class ExpressionParser extends BaseParser implements TripleParser {
    Map<Character, String> strings = Map.of('s', "et", 'c', "lear");
    @Override
    public TripleExpression parse(final String expression) {
        setSource(new StringSource(expression));
        MyExpression result;
        try {
            result = parse(1);
        } catch (IllegalArgumentException e) {
            throw error("Incorrect input");
        }
        return result;
    }
    private MyExpression parse(int priority) {
        if (priority == 4) {
            return element();
        }
        MyExpression result = parse(priority + 1);
        while (hasNext() && getOperation(priority).contains(top())) {
            char ch = next();
//            if (strings.containsKey(ch)) {
//                getWord();
//            }
            if (strings.containsKey(ch) && !strings.containsValue(getWord())) {
                throw error("Undefined token");
            }
            result = getExpression(ch, result, parse(priority + 1));
        }
        return result;
    }

    private MyExpression element() {
        char ch = next();
        if (ch == '(') {
            MyExpression result = parse(1);
            if (equals(')')) {
                next();
            }
            return result;
        } else if (ch == '-') {
            if (between(top(), '0', '9')) {
                goBack();
                return new Const(parseInt());
            } else {
                return new UnaryMin(element());
            }
        } else if (between(ch, '0', '9')) {
            goBack();
            return new Const(parseInt());
        } else if (between(ch, 'x', 'z')) {
            return new Variable(String.valueOf(ch));
        } else {
            throw error("Illegal input");
        }
    }

    private MyExpression getExpression(char first, MyExpression result, MyExpression parse) {
        return switch (first) {
            case '+' -> new Add(result, parse);
            case '-' -> new Subtract(result, parse);
            case '*' -> new Multiply(result, parse);
            case '/' -> new Divide(result, parse);
            case 's' -> new Set(result, parse);
            case 'c' -> new Clear(result, parse);
            default -> throw error("Impossible");
        };
    }

    private List<Character> getOperation(int priority) {
        return switch (priority) {
            case 1 -> List.of('s', 'c');
            case 2 -> List.of('+', '-');
            case 3 -> List.of('*', '/');
            default -> throw error("Impossible");
        };
    }
}