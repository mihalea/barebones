package ro.mihalea.cadets.barebones.logic;

import ro.mihalea.cadets.barebones.events.ErrorResponse;
import ro.mihalea.cadets.barebones.events.EventResponse;
import ro.mihalea.cadets.barebones.events.ResultResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class in which all the backend logic takes place.
 * The objects listens to any {@link Listener#interpret(String)} events triggered
 * and then act accordingly.
 */
public class Interpreter {

    public Interpreter() {
    }



    public Listener setupListener() {
        return new Listener() {
            @Override
            public EventResponse interpret(String code) {
                return new ErrorResponse(0, false, BonesError.TIMEOUT, 10);
            }
        };
    }


}
