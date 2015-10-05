package barebones.events;

import barebones.logic.BonesError;
import barebones.logic.ErrorCaught;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mm8g15 on 10/5/2015.
 */
public class ErrorResponse extends EventResponse {
    public List<ErrorCaught> errors;

    public ErrorResponse(List<ErrorCaught> errors, boolean eventConsumed) {
        super(eventConsumed);

        this.errors = errors;
    }
}
