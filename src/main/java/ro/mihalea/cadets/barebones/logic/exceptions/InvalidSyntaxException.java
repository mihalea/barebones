package ro.mihalea.cadets.barebones.logic.exceptions;

public class InvalidSyntaxException extends BonesException {
    public InvalidSyntaxException() {
        this("", -1);
    }

    public InvalidSyntaxException(String additional) {
        this(additional, -1);
    }

    public InvalidSyntaxException(int line) {
        this("", line);
    }

    public InvalidSyntaxException(String additional, int line) {
        super("Instruction does not follow the imposed syntax", additional, line);
    }
}
