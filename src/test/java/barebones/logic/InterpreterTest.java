package barebones.logic;

import barebones.events.ErrorResponse;
import barebones.events.EventResponse;
import junit.framework.TestCase;

/**
 * Created by mm8g15 on 06/10/2015.
 */
public class InterpreterTest extends TestCase {

    public void testListenerCreation() throws Exception {
        assertNotNull(getListener());
    }

    public void testTimeout() throws Exception {
        String code = "clear x; incr x; while x not 0 do; end;";
        EventResponse response = getListener().compile(code);

        assertNotNull(response);

        if(response instanceof ErrorResponse == false)
            fail("Interpreter did not exit with an error");
        else {
            ErrorResponse errorResponse = (ErrorResponse) response;
            assertNotNull(errorResponse.error);
        }
    }

    private Listener getListener() {
        Interpreter interpreter = new Interpreter();
        Listener listener = interpreter.setupListener();
        return listener;
    }
}