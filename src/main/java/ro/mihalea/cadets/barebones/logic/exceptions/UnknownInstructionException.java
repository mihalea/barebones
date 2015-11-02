package ro.mihalea.cadets.barebones.logic.exceptions;

public class UnknownInstructionException extends BonesException {
    public UnknownInstructionException() {
        this("", -1);
    }

    public UnknownInstructionException(String additional) {
        this(additional, -1);
    }

    public UnknownInstructionException(int line) {
        this("", line);
    }

    public UnknownInstructionException(String additional, int line) {
        super("Unknown instruction", additional, line);
    }
}
