package ro.mihalea.cadets.barebones.logic;

import ro.mihalea.cadets.barebones.events.EventResponse;

/**
 * Created by mircea on 02/10/15.
 */
public abstract class Listener {
    public abstract EventResponse compile(String code);
}
