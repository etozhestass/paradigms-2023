package expression.exceptions;

public class BracketSequencesException extends ParserException {
    public BracketSequencesException(String message) {
        this.message = "Not correct Bracket Sequence. No " + message;
    }
}
