package ro.mihalea.cadets.barebones.events;

import java.util.HashMap;

/**
 * Abstract class which which lays out the event specification
 */
public abstract class EventResponse {
    /**
     * Total time spent interpreting at the moment the response was sent.
     */
    public long timeElapsed;
    /**
     * Signals whether further listeners should react to the event.
     * If the field is true, then no further actions are required.
     */
    public boolean eventConsumed;

    /**
     * Template which other classes should extend containing
     * every piece of information necessary for an event
     * @param timeElapsed Total time spent interpreting at the moment the response was sent.
     * @param eventConsumed Signals whether further listeners should react to the event.
     * If the field is true, then no further actions are required.
     */
    public EventResponse(long timeElapsed, boolean eventConsumed) {
        this.timeElapsed = timeElapsed;
        this.eventConsumed = eventConsumed;
    }
}
