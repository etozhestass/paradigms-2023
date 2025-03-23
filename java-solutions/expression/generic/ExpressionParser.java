package expression.generic;

import expression.generic.Variable;
import expression.exceptions.BracketSequencesException;
import expression.exceptions.NoCorrectWhitespace;
import expression.exceptions.NoTokenException;
import expression.exceptions.UnexpectedTokenException;
import expression.generic.evaluator.Evaluator;
import expression.parser.StringSource;

import java.util.List;
import java.util.Map;

public class ExpressionParser<T> extends BaseParser<T> {
    Map<Character, String> stringsBinary = Map.of('s', "et", 'c', "lear", 'm', "od");
    private final Map<Character, String> stringsUnary = Map.of(
            'c', "ount",
            'l', "og10",
            'p', "ow10",
            'a', "bs",
            's', "quare"
    );
    private final List<Character> operations = List.of('+', '-', '*', '/', 's', 'c', 'l', 'p', 'a', 'm');
    private int pos;
    private String exp;

    public ExpressionParser(Evaluator<T> evaluator) {
        super(evaluator);
    }


    public GenericExpression<T> parse(final String expression) {
        setSource(new StringSource(expression));
        GenericExpression<T> result;
        exp = expression;
        checkCorrectBracketSequence();
        result = parse(1);
        return result;
    }
    private GenericExpression<T> parse(int priority) {
        if (priority == 4) {
            return element();
        }
        GenericExpression<T> result = parse(priority + 1);
        pos = getPos();
        checkUnexpectedToken();
        while (hasNext() && getOperation(priority).contains(top())) {
            checkUnexpectedToken();
            char ch = next();
            if (stringsBinary.containsKey(ch)) {
                skipWhitespace();
                pos = getPos();
                String word = getWord();
                if (!stringsBinary.containsValue(word)) {
                    if (word.startsWith(stringsBinary.get(ch)) || word.endsWith(stringsBinary.get(ch))) {
                        throw new NoCorrectWhitespace("Non correct whitespace's in expression \"" + exp + '"');
                    }
                    throw new UnexpectedTokenException('\'' + String.valueOf(ch) + word + "' in expression \""  + getMessageOfException());
                }
            }
            result = getBinaryExpression(ch, result, parse(priority + 1));
        }
        return result;
    }

    private GenericExpression<T> element() {
        char ch;
        try {
            pos = getPos();
            ch = next();

        } catch (StringIndexOutOfBoundsException e) {
            pos = exp.length() - 1;
            throw new NoTokenException("No last digit in expression \"" + exp + " *here ---->*\". Index: " + (exp.length() - 1));
        }
        if (ch == '(') {
            pos = getPos();
            skipWhitespace();
            if (top() == ')') {
                throw new NoTokenException("empty brackets: \"" + getMessageOfException());
            }
            GenericExpression<T> result = parse(1);
            if (equals(')')) {
                next();
            }
            return result;
        } else if (ch == '-') {
            if (isEOF()) {
                pos = getPos() - 1;
                throw new NoTokenException("for about '-' in expression \"" + getMessageOfException());
            }
            if (between(top(), '1', '9')) {
                goBack();
                return new Const<>(parse());
            } else {
                return new UnaryMin<>(element());
            }
        } else if (between(ch, '0', '9')) {
            goBack();
            return new Const<>(parse());
        } else if (between(ch, 'x', 'z')) {
            return new Variable<>(String.valueOf(ch));
        }
        String token = getToken(), word = getWord();
        if (stringsUnary.containsKey(ch) && (stringsUnary.containsValue(token)) || stringsUnary.containsValue(word)) {
            if (!isEOF() && !Character.isWhitespace(top()) && top() != '(') {
                throw new NoCorrectWhitespace("Non correct whitespace's in expression \"" + exp + '"');
            }
            return getUnaryExpression(ch, element());
        } else if (operations.contains(ch) && !stringsUnary.containsKey(ch)) {
            throw new NoTokenException("No digit for about operation '" +
                    (ch) + "' in expression \"" + getMessageOfException());
        } else if (stringsBinary.containsKey(ch) || stringsUnary.containsKey(ch)) {
            word = word.substring(1);
            String mapWord = stringsBinary.get(ch);
            String mapTwoWord = stringsUnary.get(ch);
            if (stringsUnary.containsKey(ch) && word.startsWith(mapTwoWord) && !word.equals(mapTwoWord)) {
                throw new NoCorrectWhitespace("Non correct whitespace's in expression \"" + exp + '"');
            } else if (stringsBinary.containsKey(ch) && word.charAt(word.length() - 1) != mapWord.charAt(mapWord.length() - 1) &&
                    !(stringsUnary.containsKey(ch) || stringsUnary.containsValue(word.substring(1)))) {
                throw new NoCorrectWhitespace("Non correct whitespace's in expression \"" + exp + '"');
            }
        }
        throw new NoTokenException("No last digit in expression \"" + getMessageOfException());
    }


    private GenericExpression<T> getBinaryExpression(char first, GenericExpression<T> result, GenericExpression<T> parse) {
        return switch (first) {
            case '+' -> new Add<>(result, parse);
            case '-' -> new Subtract<>(result, parse);
            case '*' -> new Multiply<>(result, parse);
            case '/' -> new Divide<>(result, parse);
            case 'm' -> new Mod<>(result, parse);
            default -> throw new AssertionError("Impossible");
        };
    }

    private GenericExpression<T> getUnaryExpression(char first, GenericExpression<T> element) {
        return switch (first) {
            case 'a' -> new Abs<>(element);
            case 's' -> new Square<>(element);
            default -> throw new AssertionError("Unexpected value: " + first);
        };
    }

    private List<Character> getOperation(int priority) {
        return switch (priority) {
            case 1 -> List.of('s', 'c', 'm');
            case 2 -> List.of('+', '-');
            case 3 -> List.of('*', '/');
            default -> throw error("Impossible");
        };
    }

    private void checkCorrectBracketSequence() {
        char ch;
        int correctBracketCounter = 0;
        while (hasNext()) {
            ch = next();
            if (ch == '(') {
                correctBracketCounter++;
            } else if (ch == ')') {
                correctBracketCounter--;
            }
            if (correctBracketCounter < 0) {
                throw new BracketSequencesException("opening parenthesis: \"" + exp + '"');
            }
        }
        if (correctBracketCounter != 0) {
            throw new BracketSequencesException("closing parenthesis: \"" + exp + '"');
        }
        toStart();
    }

    private void checkUnexpectedToken() {
        String word;
        if (hasNext() && !operations.contains(top()) && top() != '(' && top() != ')') {
            word = getWord();
            String message = !word.equals("") ? '"' + word: '\'' + String.valueOf(top());
            message += "' in expression \"" + getMessageOfException();
            throw new UnexpectedTokenException(message);
        }
    }
    private String getMessageOfException() {
        if (pos >= exp.length()) {
            pos = 0;
        }
        return exp.substring(0, pos) + " ~~~here ---->~~~ " + exp.substring(pos) + "\". Index: " + pos;
    }
}
