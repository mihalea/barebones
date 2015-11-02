package ro.mihalea.cadets.barebones.events;

/**
 * Abstract class which which lays out the event specification
 */
public abstract class EventResponse {
    /**
     * Total time spent interpreting at the moment the response was sent.
     */
    private final long timeElapsed;

    /**
     * Template which other classes should extend containing
     * every piece of information necessary for an event
     * @param timeElapsed Total time spent interpreting at the moment the response was sent.
     */
    public EventResponse(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public String getJvmInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append("Memory used: ").append((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
                / 1024 / 1024).append(" MB\n");
        builder.append("CPU time: ").append(this.timeElapsed).append(" ms");

        return builder.toString();
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }
}
