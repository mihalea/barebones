package barebones.logic;

import barebones.events.ErrorResponse;
import barebones.events.EventResponse;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mm8g15 on 06/10/2015.
 */
public class InterpreterTest extends TestCase {

    public void testListenerCreation() throws Exception {
        assertNotNull(new Interpreter().setupListener());
    }

    public void testTimeout() throws Exception {
        testErrorCode(BonesError.TIMEOUT,
                "clear x; incr x; while x not 0 do; end;");
    }

    public void testAssignBeforeClear() throws Exception {
        List<String> programs = new ArrayList<>();
        programs.add("incr x;");
        programs.add("decr x;");
        programs.add("incr x; clear x;");
        programs.add("decr x; clear x;");


        testErrorCode(BonesError.NOT_CLEARED, programs);
    }

    public void testWhileNoEnd() throws Exception {
        testErrorCode(BonesError.NO_END,
                "clear x; incr x; while x not 0 do; decr x;");
    }

    public void testWhileNoStart() throws Exception {
        testErrorCode(BonesError.NO_START, "end;");
    }

    public void testSyntaxWhile() throws Exception {
        List<String> programs = new ArrayList<>();
        //programs.add("clear x; incr x;");
        programs.add("clear x; incr x; while x;");
        programs.add("clear x; incr x; while x not;");
        programs.add("clear x; incr x; while x not 0;");

        testErrorCode(BonesError.SYNTAX_UNKNOWN, programs);

        programs.clear();
        programs.add("clear x; incr x; while x not 0 yay;");
        programs.add("clear x; incr x; while x not 1 yay;");
        programs.add("clear x; incr x; while x pi 1 yay;");
        programs.add("clear x; incr x; while x pi 1 do;");
        programs.add("clear x; incr x; while x pi 0 do;");

        testErrorCode(BonesError.SYNTAX_WHILE, programs);
    }

    public void testNoDelimiters() throws Exception {
        List<String> programs = new ArrayList<>();
        programs.add("lel");
        programs.add("clear x\nincr x");

        testErrorCode(BonesError.NO_DELIMITER, programs);
    }

    public void testSyntaxUnknown() throws Exception {
        List<String> programs = new ArrayList<>();
        programs.add("lel;");
        programs.add("hello there;");
        programs.add("How are you doing?; lel;");
        programs.add("clear x; yay");

        testErrorCode(BonesError.SYNTAX_UNKNOWN, programs);
    }

    private void testErrorCode(BonesError expected, String code) throws Exception{
        List<String> list = new ArrayList<>();
        list.add(code);
        testErrorCode(expected, list);
    }


    private void testErrorCode(BonesError expected, List<String> programs) throws Exception{
        Interpreter interpreter = new Interpreter();
        Listener listener = interpreter.setupListener();

        for (String code : programs) {
            EventResponse response = listener.compile(code);
            assertNotNull("Interpreter returned null", response);

            if (response instanceof ErrorResponse == false)
                fail("Interpreter did not exit with an error");
            else {
                ErrorResponse errorResponse = (ErrorResponse) response;
                assertNotNull("The ErrorResponse doesn't contain a valid error", errorResponse.error);
                assertSame("Interpreter did not return the expected error",
                        expected, errorResponse.error.error);
            }
        }
    }

    private EventResponse getResponse(String code) {
        Interpreter interpreter = new Interpreter();
        Listener listener = interpreter.setupListener();
        return listener.compile(code);
    }
}