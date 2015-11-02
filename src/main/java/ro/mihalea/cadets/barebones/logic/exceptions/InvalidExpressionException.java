package ro.mihalea.cadets.barebones.logic.exceptions;

public class InvalidExpressionException extends BonesException {
    public InvalidExpressionException() {
        this("", -1);
    }

    public InvalidExpressionException(String additional) {
        this(additional, -1);
    }

    public InvalidExpressionException(int line) {
        this("", line);
    }

    public InvalidExpressionException(String additional, int line) {
        super("The expression provided is invalid", additional, line);
    }
}
