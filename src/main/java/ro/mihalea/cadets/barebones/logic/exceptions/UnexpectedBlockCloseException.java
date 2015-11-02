package ro.mihalea.cadets.barebones.logic.exceptions;

public class UnexpectedBlockCloseException extends BonesException {
    public UnexpectedBlockCloseException() {
        this("", -1);
    }

    public UnexpectedBlockCloseException(String additional) {
        this(additional, -1);
    }

    public UnexpectedBlockCloseException(int line) {
        this("", line);
    }

    public UnexpectedBlockCloseException(String additional, int line) {
        super("A block has been expected where none where expected to", additional, line);
    }
}
