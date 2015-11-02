package ro.mihalea.cadets.barebones.logic.exceptions;

public class ExpectedNumberException extends BonesException {
    public ExpectedNumberException() {
        this("", -1);
    }

    public ExpectedNumberException(String additional) {
        this(additional, -1);
    }

    public ExpectedNumberException(int line) {
        this("", line);
    }

    public ExpectedNumberException(String additional, int line) {
        super("Value passed as an argument could not be parsed as a number", additional, line);
    }
}
