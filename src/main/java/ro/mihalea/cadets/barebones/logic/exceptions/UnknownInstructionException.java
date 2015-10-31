package ro.mihalea.cadets.barebones.logic.exceptions;

/**
 * Exception thrown when the first identifier is not a recognized keyword
 */
public class UnknownInstructionException extends BonesException {
    /**
     * Protected constructor so that only subclasses can directly create
     * an instance of this class
     *
     * @param line    Line where the error got caught
     */
    public UnknownInstructionException(int line) {
        super("Unknown instruction", line);
    }
}
