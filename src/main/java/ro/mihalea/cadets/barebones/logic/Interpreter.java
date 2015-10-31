package ro.mihalea.cadets.barebones.logic;

import ro.mihalea.cadets.barebones.events.ErrorResponse;
import ro.mihalea.cadets.barebones.events.EventResponse;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidCharacterException;
import ro.mihalea.cadets.barebones.logic.exceptions.NotTerminatedException;
import ro.mihalea.cadets.barebones.logic.units.Decoder;
import ro.mihalea.cadets.barebones.logic.units.Memory;
import ro.mihalea.cadets.barebones.logic.units.Processor;

/**
 * Class in which all the backend logic takes place.
 * The objects listens to any {@link Listener#interpret(String)} events triggered
 * and then act accordingly.
 */
public class Interpreter {

    private Decoder decoder;
    private Processor processor;

    /**
     * Instantiates the fields
     */
    public Interpreter() {
        decoder = new Decoder();
        processor = new Processor(new Memory());
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
     * TO BE ALTERED. DO NOT LEAVE IT LIKE THIS.
     * Runs the program
     * @param code Lines of barebones to be run
     * @return Memory at the time it finished running
     */
    public Memory run(String code) {
        try {
            decoder.append(code);
            if (decoder.canFetch()) {
                processor.load(decoder.fetch());
                return processor.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
