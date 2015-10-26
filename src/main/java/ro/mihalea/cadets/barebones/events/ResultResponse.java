package ro.mihalea.cadets.barebones.events;

import java.util.HashMap;

/**
 * Response that signal successful interpretation
 */
public class ResultResponse extends EventResponse {
    /**
     * Map holding all the variables used by the interpreter
     * with all of their final values.
     */
    public HashMap<String, Long> vars;

    /**
     * Creates a response that signals that the interpreter has finished
     * running successfully and has returned a list of variables.
     * @param timeElapsed Total time spent interpreting at the moment the event was sent.
     * @param eventConsumed Signals whether further listeners should react to the event.
     * If the field is true, then no further actions are required.
     * @param vars Map of the variables as they looked when the interpreter finished running.
     */
    public ResultResponse(long timeElapsed, boolean eventConsumed, HashMap<String, Long> vars) {
        super(timeElapsed, eventConsumed);
        this.vars = vars;
    }
}
