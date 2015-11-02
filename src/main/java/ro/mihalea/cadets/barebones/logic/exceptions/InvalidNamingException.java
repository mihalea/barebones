package ro.mihalea.cadets.barebones.logic.exceptions;

public class InvalidNamingException extends BonesException {
    public InvalidNamingException() {
        this("", -1);
    }

    public InvalidNamingException(String additional) {
        this(additional, -1);
    }

    public InvalidNamingException(int line) {
        this("", line);
    }

    public InvalidNamingException(String additional, int line) {
        super("Variable does not meet the naming restrictions", additional, line);
    }
}
