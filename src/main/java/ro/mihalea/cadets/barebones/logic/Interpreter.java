package ro.mihalea.cadets.barebones.logic;

import ro.mihalea.cadets.barebones.Listener;
import ro.mihalea.cadets.barebones.events.DebugResponse;
import ro.mihalea.cadets.barebones.events.ErrorResponse;
import ro.mihalea.cadets.barebones.events.EventResponse;
import ro.mihalea.cadets.barebones.events.ResultResponse;
import ro.mihalea.cadets.barebones.logic.exceptions.*;
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

            @Override
            public EventResponse loadDebug(String code) {
                try {
                    Interpreter inter = Interpreter.this;
                    inter.load(code);
                    return new DebugResponse(0, null, processor.getLineIndex());

                } catch (BonesException e) {
                    return new ErrorResponse(0, e);
                }
            }

            @Override
            public EventResponse nextDebug() {
                try {
                    Memory memory = Interpreter.this.next();
                    return new DebugResponse(0, memory, processor.getLineIndex());

                } catch (NotAssignedException|InvalidExpressionException|InvalidCharacterException e) {
                    return new ErrorResponse(0, e);
                }
            }
        };
    }

    /**
     * Runs the program
     * @param code Lines of barebones to be run
     * @return Memory at the time it finished running, null if the code is not compilable
     */
    public Memory run(String code) throws BonesException {
        this.load(code);
        return processor.run();
    }

    public void load(String code) throws BonesException {
        decoder.append(code);
        if(decoder.canFetch())
            processor.load(decoder.fetch());
        else
            throw new BlockUnfinishedException();
    }

    public Memory next() throws NotAssignedException, InvalidCharacterException, InvalidExpressionException {
        int startIndex = processor.getLineIndex();
        int index;
        Memory memory;
        do {
            memory = processor.next();
            index = processor.getLineIndex();
        } while(startIndex == index && index != -1);

        return memory;
    }

}
