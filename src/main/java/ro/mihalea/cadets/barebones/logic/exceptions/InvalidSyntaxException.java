package ro.mihalea.cadets.barebones.logic.exceptions;

/**
 * Exception thrown when an instruction does not follow the specified syntax
 */
public class InvalidSyntaxException extends BonesException {
    /**
     * Protected constructor so that only subclasses can directly create
     * an instance of this class
     *
     * @param line    Line where the error got caught
     */
    public InvalidSyntaxException(int line) {
        super("Instruction does not meet expected syntax specification.", line);
    }
}
