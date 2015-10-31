package ro.mihalea.cadets.barebones.logic;

import ro.mihalea.cadets.barebones.events.ErrorResponse;
import ro.mihalea.cadets.barebones.events.EventResponse;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidCharacterException;
import ro.mihalea.cadets.barebones.logic.exceptions.NotTerminatedException;
import ro.mihalea.cadets.barebones.logic.units.Fetcher;

/**
 * Class in which all the backend logic takes place.
 * The objects listens to any {@link Listener#interpret(String)} events triggered
 * and then act accordingly.
 */
public class Interpreter {
    /**
     * Handles the raw instruction buffer
     */
    private Fetcher fetcher;

    /**
     * Instantiates the fields
     */
    public Interpreter() {
        fetcher = new Fetcher();
    }

    /**
     * Creates a new listener which should handle all the events
     * created by the GUI
     * @return A new listener
     */
    public Listener setupListener() {
        return new Listener() {
            @Override
            public EventResponse interpret(String code) {
                Interpreter interpreter = Interpreter.this;
                return new ErrorResponse(0, new NotTerminatedException(100));
            }
        };
    }

    /**
     * Clears all the variables and creates a new environment
     */
    public void clear() {
        fetcher.clear();
    }

    public void addInstructions(String program)
            throws NotTerminatedException, InvalidCharacterException {
        fetcher.add(program);
    }

    public void decode() {

    }


}
