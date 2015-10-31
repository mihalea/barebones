package ro.mihalea.cadets.barebones.logic.exceptions;

/**
 * Created by Mircea on 31-Oct-15.
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
