package ro.mihalea.cadets.barebones.logic;

import ro.mihalea.cadets.barebones.events.ErrorResponse;
import ro.mihalea.cadets.barebones.events.EventResponse;
import ro.mihalea.cadets.barebones.events.ResultResponse;
import ro.mihalea.cadets.barebones.logic.exceptions.BlockUnfinishedException;
import ro.mihalea.cadets.barebones.logic.exceptions.BonesException;
import ro.mihalea.cadets.barebones.logic.units.Decoder;
import ro.mihalea.cadets.barebones.logic.units.Memory;
import ro.mihalea.cadets.barebones.logic.units.Processor;

/**
 * Class in which all the backend logic takes place.
 * The objects listens to any {@link Listener#interpret(String)} events triggered
 * and then act accordingly.
 */
public class Interpreter {

    private final Decoder decoder;
    private final Processor processor;

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
                try {
                    Memory memory = Interpreter.this.run(code);
                    return new ResultResponse(0, memory);
                } catch (BonesException exception) {
                    return new ErrorResponse(0, exception);
                }

            }
        };
    }

    /**
     * Runs the program
     * @param code Lines of barebones to be run
     * @return Memory at the time it finished running, null if the code is not compileable
     */
    public Memory run(String code) throws BonesException {
        decoder.append(code);
        if (decoder.canFetch()) {
            processor.load(decoder.fetch());
            return processor.run();
        } else
            throw new BlockUnfinishedException();
    }

}
