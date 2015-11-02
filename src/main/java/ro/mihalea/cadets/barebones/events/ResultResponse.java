package ro.mihalea.cadets.barebones.events;

import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.HashMap;

/**
 * Response that signal successful interpretation
 */
public class ResultResponse extends EventResponse {
    /**
     * Map holding all the variables used by the interpreter
     * with all of their final values.
     */
    public final Memory memory;

    /**
     * Creates a response that signals that the interpreter has finished
     * running successfully and has returned a list of variables.
     * @param timeElapsed Total time spent interpreting at the moment the event was sent.
     * @param memory Map of the variables as they looked when the interpreter finished running.
     */
    public ResultResponse(long timeElapsed, Memory memory) {
        super(timeElapsed);
        this.memory = memory;
    }
}
