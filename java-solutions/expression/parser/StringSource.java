package expression.parser;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class StringSource implements CharSource {
    protected int pos;
    private final String data;

    public int getPos() {
        return pos;
    }

    public StringSource(final String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() throws IndexOutOfBoundsException {
        return data.charAt(pos++);
    }

    @Override
    public char curr() throws IndexOutOfBoundsException {
        return data.charAt(pos);
    }

    @Override
    public void goBack() throws IndexOutOfBoundsException {
        pos--;
        while (Character.isWhitespace(data.charAt(pos)) && pos > 0) {
            pos--;
        }
    }

    @Override
    public void goStartWord() {
        while (!Character.isWhitespace(data.charAt(pos)) && pos > 0) {
            if (Character.isWhitespace(data.charAt(pos - 1))) {
                break;
            }
            pos--;
        }
    }

    @Override
    public void goEndWord() {
        while (!Character.isWhitespace(data.charAt(pos)) && pos < data.length()) {
            if (Character.isWhitespace(data.charAt(pos + 1))) {
                break;
            }
            pos++;
        }
    }

    @Override
    public void toStart() {
        pos = 0;
    }

    @Override
    public IllegalArgumentException error(final String message) {
        return new IllegalArgumentException(pos + ": " + message);
    }

    @Override
    public void skipWhitespace() {
        while (hasNext() && Character.isWhitespace(data.charAt(pos))) {
            pos++;
        }
    }
}
