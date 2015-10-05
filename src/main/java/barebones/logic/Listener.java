package barebones.logic;

import barebones.events.EventResponse;

/**
 * Created by mircea on 02/10/15.
 */
public abstract class Listener {
    public abstract EventResponse compile(String code);
}
