package ro.mihalea.cadets.barebones.logic;

/**
 * Wrapper object which contains a {@link ro.mihalea.cadets.barebones.logic.BonesError} and a line number
 */
public class ErrorCaught {
    /**
     * Error with message and code.
     */
    public BonesError error;
    /**
     * Line number on which the error has been thrown.
     */
    public int line;

    /**
     * Creates the wrapper object
     * @param error Error returned by the interpreter.
     * @param line Line number on which the error got thrown.
     */
    public ErrorCaught(BonesError error, int line) {
        this.error = error;
        this.line = line;
    }
}
