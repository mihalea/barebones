package ro.mihalea.cadets.barebones.logic.exceptions;

public class NotAssignedException extends BonesException {
    public NotAssignedException() {
        this("", -1);
    }

    public NotAssignedException(String additional) {
        this(additional, -1);
    }

    public NotAssignedException(int line) {
        this("", line);
    }

    public NotAssignedException(String additional, int line) {
        super("Variable used before it had a value assigned", additional, line);
    }
}
