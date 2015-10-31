package ro.mihalea.cadets.barebones.logic.exceptions;

/**
 * Exception thrown when one ore more instructions have not been
 * correctly terminated using the semicolon
 */
public class NotTerminatedException extends BonesException {
    /**
     * Protected constructor so that only subclasses can directly create
     * an instance of this class
     *
     * @param line    Line where the error got caught
     */
    public NotTerminatedException(int line) {
        super("One or more statements have not been " +
                "correctly terminated using the semicolon"
                , line);
    }
}
