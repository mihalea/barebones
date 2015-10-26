package barebones.events;

import barebones.logic.BonesError;
import barebones.logic.ErrorCaught;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mm8g15 on 10/5/2015.
 */
public class ErrorResponse extends EventResponse {

    public ErrorCaught error;

    /**
     * Creates a response that signals that the interpreter
     * has not finished running successfully.
     * @param timeElapsed Time spent interpreting in milliseconds
     * @param eventConsumed Shows whether further listeners should react to the event
     * @param error Details about the error
     */
    public ErrorResponse(long timeElapsed, boolean eventConsumed, ErrorCaught error) {
        super(timeElapsed, eventConsumed);
        this.error = error;
    }
}
