package expression.parser;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface CharSource {
    boolean hasNext();
    char next();
    char curr();
    int getPos();
    void goBack();
    void goStartWord();
    void goEndWord();
    void toStart();
    IllegalArgumentException error(String message);

    void skipWhitespace();
}
