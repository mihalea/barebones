package ro.mihalea.cadets.barebones.events;

import ro.mihalea.cadets.barebones.logic.units.Memory;

/**
 * Created by Mircea on 02-Nov-15.
 */
public class DebugResponse extends EventResponse {
    private final Memory memory;
    private final int lineIndex;

    /**
     * Template which other classes should extend containing
     * every piece of information necessary for an event
     *
     * @param timeElapsed Total time spent interpreting at the moment the response was sent.
     */
    public DebugResponse(long timeElapsed, Memory memory, int lineIndex) {
        super(timeElapsed);
        this.memory = memory;
        this.lineIndex = lineIndex;
    }

    public Memory getMemory() {
        return memory;
    }

    public int getLineIndex() {
        return lineIndex;
    }
}
