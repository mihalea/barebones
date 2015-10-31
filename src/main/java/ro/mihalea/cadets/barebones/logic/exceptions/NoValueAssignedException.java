package ro.mihalea.cadets.barebones.logic.exceptions;

/**
 * Created by Mircea on 31-Oct-15.
 */
public class NoValueAssignedException extends BonesException {
    /**
     * Protected constructor so that only subclasses can directly create
     * an instance of this class
     *
     * @param line    Line where the error got caught
     */
    public NoValueAssignedException(int line) {
        super("No value has been assigned to the variable", line);
    }
}
