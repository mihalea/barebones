package ro.mihalea.cadets.barebones.logic.exceptions;

/**
 * Exception thrown when a variable does not meet the naming specifications
 */
public class InvalidVariableNameException extends BonesException {
    /**
     * Protected constructor so that only subclasses can directly create
     * an instance of this class
     *
     * @param line    Line where the error got caught
     */
    public InvalidVariableNameException(int line) {
        super("Variable does not meet the naming expectation", line);
    }
}
