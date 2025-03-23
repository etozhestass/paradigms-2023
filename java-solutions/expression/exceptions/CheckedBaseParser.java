package expression.exceptions;

import expression.parser.CharSource;
public class CheckedBaseParser {
    private CharSource source;

    protected void setSource(CharSource source) {
        this.source = source;
    }

    protected IllegalArgumentException error(final String message) {
        return source.error(message);
    }

    protected String getWord() {
        StringBuilder sb = new StringBuilder();
        source.skipWhitespace();
        while (source.hasNext() && (Character.isLetter(source.curr()) || Character.isDigit(source.curr()))) {
            sb.append(next());
        }
        return sb.toString();
    }

    protected String getLasWord() {
        goBack();
        goStartWord();
        return getWord();
    }
    protected String getToken() {
        StringBuilder sb = new StringBuilder();
        while (source.hasNext() && (Character.isLetter(source.curr()) && Character.isDigit(source.curr()))) {
            sb.append(next());
        }
        return sb.toString();
    }
    protected boolean isNextWhitespace() {
        return hasNext() && Character.isWhitespace(source.next());
    }

    protected boolean hasNext() {
        source.skipWhitespace();
        return source.hasNext();
    }

    protected boolean isEOF() {
        return !source.hasNext();
    }

    protected char top() {
        return source.curr();
    }

    protected char next() {
        source.skipWhitespace();
        return source.next();
    }
    protected int getPos() {
        return source.getPos();
    }
    protected void toStart() {
        source.toStart();
    }

    protected int parseInt() {
        StringBuilder sb = new StringBuilder();
        sb.append(source.next());
        while (source.hasNext() && Character.isDigit(source.curr())) {
            sb.append(source.next());
        }
        try {
            return Integer.parseInt(sb.toString());
        } catch (NumberFormatException e) {
            throw new OverflowException(sb.toString());
        }
    }

    protected void nextChar() {
        source.next();
    }

    protected void goStartWord() {
        source.goStartWord();
    }

    protected void goEndWord() {
        source.goEndWord();
    }

    protected void skipWhitespace() {
        source.skipWhitespace();
    }

    protected boolean between(char x, char from, char to) {
        return from <= x && x <= to;
    }

    protected boolean equals(char ch) {
        return top() == ch;
    }

    protected boolean equals(char ch1, char ch2) {
        return top() == ch1 || top() == ch2;
    }

    protected void goBack() {
        source.goBack();
    }
}