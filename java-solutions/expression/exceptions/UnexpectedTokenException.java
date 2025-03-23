package expression.exceptions;

public class UnexpectedTokenException extends ParserException{
    public UnexpectedTokenException(String token) {
        this.message = "Unexpected token: " + token;
    }

    public UnexpectedTokenException(char token) {
        this.message = "Unexpected token: '" + token + "'";
    }
}
