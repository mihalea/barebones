package ro.mihalea.cadets.barebones.events;

/**
 * Abstract class which which lays out the event specification
 */
public abstract class EventResponse {
    /**
     * Total time spent interpreting at the moment the response was sent.
     */
    public final long timeElapsed;

    /**
     * Template which other classes should extend containing
     * every piece of information necessary for an event
     * @param timeElapsed Total time spent interpreting at the moment the response was sent.
     */
    public EventResponse(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }
}
