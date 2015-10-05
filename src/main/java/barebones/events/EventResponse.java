package barebones.events;

import java.util.HashMap;

/**
 * Created by mircea on 02/10/15.
 */
public abstract class EventResponse {
    public long timeElapsed;
    public boolean eventConsumed;

    public EventResponse(long timeElapsed, boolean eventConsumed) {
        this.timeElapsed = timeElapsed;
        this.eventConsumed = eventConsumed;
    }
}
