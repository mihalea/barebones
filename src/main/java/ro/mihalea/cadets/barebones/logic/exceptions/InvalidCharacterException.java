package ro.mihalea.cadets.barebones.logic.exceptions;

public class InvalidCharacterException extends BonesException {
    public InvalidCharacterException() {
        this("", -1);
    }

    public InvalidCharacterException(String additional) {
        this(additional, -1);
    }

    public InvalidCharacterException(int line) {
        this("", line);
    }

    public InvalidCharacterException(String additional, int line) {
        super("Instruction contains one or more invalid characters", additional, line);
    }
}
