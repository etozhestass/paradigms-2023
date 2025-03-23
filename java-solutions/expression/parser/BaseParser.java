package expression.parser;

public class BaseParser {
    private CharSource source;

    protected void setSource(CharSource source) {
        this.source = source;
    }

    protected IllegalArgumentException error(final String message) {
        return source.error(message);
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

    public void goBack() {
        source.goBack();
    }

    public int parseInt() {
        StringBuilder sb = new StringBuilder();
        sb.append(source.next());
        source.skipWhitespace();
        while (source.hasNext() && Character.isDigit(source.curr())) {
            sb.append(source.next());
        }
        return Integer.parseInt(sb.toString());
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