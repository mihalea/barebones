package barebones.events;

import java.util.HashMap;

/**
 * Created by mircea on 02/10/15.
 */
public abstract class EventResponse {
    public boolean eventConsumed;

    public EventResponse(boolean eventConsumed) {
        this.eventConsumed = eventConsumed;
    }
}
