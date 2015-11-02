package ro.mihalea.cadets.barebones.logic.exceptions;

/**
 * Model for the exceptions thrown by the interpreter
 */
public abstract class BonesException extends Exception {
    /**
     * Final message defined by the developer describing the error
     */
    private String message;

    /**
     * Additional info provided on a case by case basis
     */
    private String additional;

    /**
     * Line where the error got thrown
     */
    private int line = -1;

    public BonesException(String message, String additional, int line) {
        this.message = message;
        this.additional = additional;
        this.line = line;
    }

    public String getMessage() {
        return message + (additional.isEmpty() ? "" : ": " + additional);
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }
}
