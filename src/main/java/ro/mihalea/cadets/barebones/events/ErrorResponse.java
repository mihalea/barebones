package ro.mihalea.cadets.barebones.events;

import ro.mihalea.cadets.barebones.logic.exceptions.BonesException;

/**
 * Response that signals an error
 */
public class ErrorResponse extends EventResponse {
    /**
     * Error sent to the listeners.
     */
    private final BonesException exception;

    /**
     * Creates a response that signals that the interpreter
     * has not finished running successfully.
     * @param timeElapsed Total time spent interpreting at the moment the error was thrown.
     * @param exception Holds the exception that halted the interpretation
     */
    public ErrorResponse(long timeElapsed, BonesException exception) {
        super(timeElapsed);
        this.exception = exception;
    }

    public BonesException getException() {
        return exception;
    }
}
