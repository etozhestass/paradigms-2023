package expression.generic;

import expression.generic.evaluator.Evaluator;
import expression.parser.CharSource;

public class BaseParser<T> {
    private CharSource source;
    private final Evaluator<T> evaluator;

    public BaseParser(Evaluator<T> evaluator) {
        this.evaluator = evaluator;
    }

    protected void setSource(CharSource source) {
        this.source = source;
    }

    protected IllegalArgumentException error(final String message) {
        return source.error(message);
    }
    protected void toStart() {
        source.toStart();
    }
    protected int getPos() {
        return source.getPos();
    }
    protected void skipWhitespace() {
        source.skipWhitespace();
    }

    public String getWord() {
        StringBuilder sb = new StringBuilder();
        source.skipWhitespace();
        while (source.hasNext() && Character.isLetter(source.curr())) {
            sb.append(next());
        }
        return sb.toString();
    }

    protected boolean hasNext() {
        source.skipWhitespace();
        return source.hasNext();
    }

    public char top() {
        source.skipWhitespace();
        return source.curr();
    }

    public char next() {
        source.skipWhitespace();
        return source.next();
    }

    protected boolean isEOF() {
        return !source.hasNext();
    }

    protected String getToken() {
        StringBuilder sb = new StringBuilder();
        while (source.hasNext() && (Character.isLetter(source.curr()) && Character.isDigit(source.curr()))) {
            sb.append(next());
        }
        return sb.toString();
    }

    public void goBack() {
        source.goBack();
    }

    public T parse() {
        StringBuilder sb = new StringBuilder();
        sb.append(source.next());
        source.skipWhitespace();
        while (source.hasNext() && (Character.isDigit(source.curr()) || source.curr() == '.')) {
            sb.append(source.next());
        }
        return evaluator.parse(sb.toString());
    }


    public boolean between(char x, char from, char to) {
        return from <= x && x <= to;
    }

    public boolean equals(char ch) {
        return top() == ch;
    }

    public boolean equals(char ch1, char ch2) {
        return top() == ch1 || top() == ch2;
    }
}