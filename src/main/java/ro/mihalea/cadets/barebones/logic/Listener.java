package ro.mihalea.cadets.barebones.logic;

import ro.mihalea.cadets.barebones.events.EventResponse;

/**
 * Abstract listener which makes the link between
 * the UI and the backend
 */
public abstract class Listener {

    /**
     * Event that signals the fact that a program is waiting to be interpreted
     * @param code Raw lines of code
     * @return A response stating if the interpreter succeeded
     * and it may eventually contain an error
     */
    public abstract EventResponse interpret(String code);
}
