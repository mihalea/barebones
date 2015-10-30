package ro.mihalea.cadets.barebones.logic;

import ro.mihalea.cadets.barebones.events.ErrorResponse;
import ro.mihalea.cadets.barebones.events.EventResponse;
import ro.mihalea.cadets.barebones.events.ResultResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @return
     */
    public Listener setupListener() {
        return new Listener() {
            @Override
            public EventResponse interpret(String code) {
                Interpreter interpreter = Interpreter.this;
                return new ErrorResponse(0, false, BonesError.TIMEOUT, 10);
            }
        };
    }

    /**
     * Clears all the variables and creates a new environment
     */
    public void clear() {

    }

    public void fetch(String program) {
        fetcher.add(program);
    }


}
