package ro.mihalea.cadets.barebones.events;

import ro.mihalea.cadets.barebones.logic.BonesError;

/**
 * Response that signals an error
 */
public class ErrorResponse extends EventResponse {
    /**
     * Error sent to the listeners.
     */
    public BonesError error;

    /**
     * Line on which the error got caught
     */
    public int line;

    /**
     * Creates a response that signals that the interpreter
     * has not finished running successfully.
     * @param timeElapsed Total time spent interpreting at the moment the error was thrown.
     * @param eventConsumed Shows whether further listeners should react to the event.
     * @param error Details about the error.
     * @param line Line number on which the error got caught.
     */
    public ErrorResponse(long timeElapsed, boolean eventConsumed, BonesError error, int line) {
        super(timeElapsed, eventConsumed);
        this.error = error;
        this.line = line;
    }
}
