package barebones.events;

import java.util.HashMap;

/**
 * Created by mircea on 05/10/15.
 */
public class ResultResponse extends EventResponse {
    public HashMap<String, Long> vars;

    public ResultResponse(HashMap<String, Long> vars, boolean eventConsumed) {
        super(eventConsumed);

        this.vars = vars;
    }
}
