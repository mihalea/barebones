package ro.mihalea.cadets.barebones.logic.exceptions;

/**
 * Model for the exceptions thrown by the interpreter
 */
public abstract class BonesException extends Exception {
    /**
     * Final message defined by the developer describing the error
     */
    protected final String message;

    /**
     * Line where the error got thrown
     */
    protected int line;

    /**
     * Protected constructor so that only subclasses can directly create
     * an instance of this class
     * @param message Descriptive message to be shown to the user
     * @param line Line where the error got caught
     */
    public BonesException(String message, int line) {
        super(message);
        this.line = line;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
