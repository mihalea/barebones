package ro.mihalea.cadets.barebones.logic.exceptions;

public class TimeoutException extends BonesException {
    public TimeoutException() {
        this("", -1);
    }

    public TimeoutException(String additional) {
        this(additional, -1);
    }

    public TimeoutException(int line) {
        this("", line);
    }

    public TimeoutException(String additional, int line) {
        super("Interpreter has exceeded the maximum runtime", additional, line);
    }
}
