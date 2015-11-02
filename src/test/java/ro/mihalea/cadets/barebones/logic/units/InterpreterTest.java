package ro.mihalea.cadets.barebones.logic.units;

import junit.framework.TestCase;
import ro.mihalea.cadets.barebones.logic.Interpreter;
import ro.mihalea.cadets.barebones.Listener;

/**
 * Created by mm8g15 on 06/10/2015.
 */
public class InterpreterTest extends TestCase {

    public void testListenerCreation() throws Exception{
        Interpreter interpreter = new Interpreter();
        assertNotNull(interpreter);

        Listener listener = interpreter.setupListener();
        assertNotNull(listener);
    }

    public void testIncrement() throws Exception {
        Interpreter interpreter = new Interpreter();
        Memory memory = interpreter.run("incr x;");
        assertEquals(1, memory.get("x"));

        memory = interpreter.run("incr x; incr y;");
        assertEquals(1, memory.get("x"));
        assertEquals(1, memory.get("y"));

        memory = interpreter.run("incr x x;");
        assertEquals(2, memory.get("x"));

        memory = interpreter.run("incr x y;");
        assertEquals(1, memory.get("x"));
        assertEquals(1, memory.get("y"));
    }

    public void testDecrement() throws Exception{
        Interpreter interpreter = new Interpreter();
        Memory memory = interpreter.run("decr x;");
        assertEquals(-1, memory.get("x"));

        memory = interpreter.run("decr x; decr y;");
        assertEquals(-1, memory.get("x"));
        assertEquals(-1, memory.get("y"));

        memory = interpreter.run("decr x x;");
        assertEquals(-2, memory.get("x"));

        memory = interpreter.run("decr x y;");
        assertEquals(-1, memory.get("x"));
        assertEquals(-1, memory.get("y"));
    }

    public void testClear() throws Exception {
        Interpreter interpreter = new Interpreter();
        Memory memory = interpreter.run("decr x; clear x;");
        assertEquals(0, memory.get("x"));

        memory = interpreter.run("decr x; clear x; incr x;");
        assertEquals(1, memory.get("x"));
    }

    public void testInit() throws Exception {
        Interpreter interpreter = new Interpreter();
        Memory memory = interpreter.run("init x = 25;");
        assertEquals(25, memory.get("x"));

        memory = interpreter.run("incr x; init x = 4761;");
        assertEquals(4761, memory.get("x"));
    }

    public void testWhile() throws Exception {
        Interpreter interpreter = new Interpreter();
        Memory memory = interpreter.run("init x = 25; clear y; while x not 0 do; incr y; decr x; end;");
        assertEquals(25, memory.get("y"));

        memory = interpreter.run("init x = 25; clear z; " +
                "while x not 0 do; " +
                "init y = 10; " +
                "while y not 0 do; " +
                "incr z; decr y;" +
                "end;" +
                "decr x;" +
                "end;");
        assertEquals(250, memory.get("z"));
    }

    public void testCopy() throws Exception {
        Interpreter interpreter = new Interpreter();
        Memory memory = interpreter.run("init x = 25; copy x to y; incr y;");
        assertEquals(26, memory.get("y"));
    }

    public void testMultiply() throws Exception {
        Interpreter interpreter = new Interpreter();

        Memory memory = interpreter.run("init X = 24;" +
                "init Y = 54;\n" +
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
        assertEquals(1296, memory.get("Z"));
    }

    public void testFactorial() throws Exception {
        Interpreter interpreter = new Interpreter();

        Memory memory = interpreter.run("init N = 5;" +
                "clear F;\n" +
                "incr F;\n" +
                "decr N;\n" +
                "\n" +
                "while N not 0 do;\n" +
                "  copy F to G;\n" +
                "  while G not 0 do;\n" +
                "    copy N to H;\n" +
                "    while H not 0 do;\n" +
                "      incr F;\n" +
                "      decr H;\n" +
                "    end;\n" +
                "    decr G;\n" +
                "  end;\n" +
                "  decr N;\n" +
                "end;");
        assertEquals(120, memory.get("F"));
    }

    public void testFibonacci() throws Exception {
        Interpreter interpreter = new Interpreter();

        Memory memory = interpreter.run("init N = 10;" +
                "clear F;\n" +
                "clear G;\n" +
                "incr G;\n" +
                "\n" +
                "while N not 0 do;\n" +
                "  copy G to H;\n" +
                "  while F not 0 do;\n" +
                "    incr H;\n" +
                "    decr F;\n" +
                "  end;\n" +
                "\n" +
                "  copy G to F;\n" +
                "  copy H to G;\n" +
                "\n" +
                "  decr N;\n" +
                "end;");
        assertEquals(55, memory.get("F"));
    }

    public void testComment() throws Exception {
        Interpreter interpreter = new Interpreter();
        Memory memory = interpreter.run("incr x;\n" +
                "#incr x");
        assertEquals(1, memory.get("x"));

        memory = interpreter.run("incr x;#incr x;");
        assertEquals(1, memory.get("x"));

        memory = interpreter.run("#: heya\n" +
                "how you doing?\n" +
                "incr x:#\n" +
                "incr x;");
        assertEquals(1, memory.get("x"));

        memory = interpreter.run("#: multiline\n" +
                "abcd:# incr x; #: bestie mica :# incr y; #incr x;\n" +
                "incr x;");
        assertEquals(2, memory.get("x"));
        assertEquals(1, memory.get("y"));
    }
}