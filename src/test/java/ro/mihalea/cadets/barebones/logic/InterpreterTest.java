package ro.mihalea.cadets.barebones.logic;

import ro.mihalea.cadets.barebones.events.ErrorResponse;
import ro.mihalea.cadets.barebones.events.EventResponse;
import ro.mihalea.cadets.barebones.events.ResultResponse;
import junit.framework.TestCase;
import ro.mihalea.cadets.barebones.logic.BonesError;
import ro.mihalea.cadets.barebones.logic.Interpreter;
import ro.mihalea.cadets.barebones.logic.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mm8g15 on 06/10/2015.
 */
public class InterpreterTest extends TestCase {

    public void testListenerCreation() {
        Interpreter interpreter = new Interpreter();
        assertNotNull(interpreter);

        Listener listener = interpreter.setupListener();
        assertNotNull(listener);
    }

    public void testCodeOkay() throws Exception {
        Interpreter interpreter = new Interpreter();
        Listener listener = interpreter.setupListener();

        EventResponse response =
                listener.interpret("clear x; incr x; while x not 0 do; decr x; end;");

        assertNotNull(response);

        if(response instanceof ResultResponse == false)
            fail("The returned event is not the expected type");
        else {
            ResultResponse resultResponse = (ResultResponse) response;
            assertNotNull(resultResponse.vars);

            if(resultResponse.vars.size() == 0)
                fail("There are no returned variabled");
        }
    }

    public void testCodeOkayWithMultiplication() throws Exception {
        Interpreter interpreter = new Interpreter();
        Listener listener = interpreter.setupListener();

        EventResponse response =
                listener.interpret("clear X;\n" +
                        "incr X;\n" +
                        "incr X;\n" +
                        "clear Y;\n" +
                        "incr Y;\n" +
                        "incr Y;\n" +
                        "incr Y;\n" +
                        "clear Z;\n" +
                        "while X not 0 do;\n" +
                        "   clear W;\n" +
                        "   while Y not 0 do;\n" +
                        "      incr Z;\n" +
                        "      incr W;\n" +
                        "      decr Y;\n" +
                        "   end;\n" +
                        "   while W not 0 do;\n" +
                        "      incr Y;\n" +
                        "      decr W;\n" +
                        "   end;\n" +
                        "   decr X;\n" +
                        "end;");

        assertNotNull(response);

        if(response instanceof ResultResponse == false)
            fail("The returned event is not the expected type");
        else {
            ResultResponse resultResponse = (ResultResponse) response;
            assertNotNull(resultResponse.vars);

            if(resultResponse.vars.size() == 0)
                fail("There are no returned variabled");

            if(resultResponse.vars.get("Z") != 6)
                fail("The multiplication result is not correct");
        }
    }

    public void testTimeout() throws Exception {
        testErrorCode(BonesError.TIMEOUT,
                "clear x; incr x; while x not 0 do; incr x; end;");
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

    public void testSyntaxUnknown() throws Exception {
        List<String> programs = new ArrayList<>();
        programs.add("lel;");
        programs.add("hello there;");
        programs.add("How are you doing?; lel;");
        programs.add("clear x; yay");
        programs.add("lel");
        programs.add("clear x\nincr x");

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
            EventResponse response = listener.interpret(code);
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
}