package ro.mihalea.cadets.barebones.logic.units;

import junit.framework.TestCase;
import ro.mihalea.cadets.barebones.logic.Interpreter;
import ro.mihalea.cadets.barebones.logic.Listener;
import ro.mihalea.cadets.barebones.logic.exceptions.NotAssignedException;

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

    public void testIncrement() throws NotAssignedException {
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

    public void testDecrement() throws NotAssignedException{
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

    public void testClear() throws NotAssignedException {
        Interpreter interpreter = new Interpreter();
        Memory memory = interpreter.run("decr x; clear x;");
        assertEquals(0, memory.get("x"));

        memory = interpreter.run("decr x; clear x; incr x;");
        assertEquals(1, memory.get("x"));
    }

    public void testInit() throws NotAssignedException {
        Interpreter interpreter = new Interpreter();
        Memory memory = interpreter.run("init x = 25;");
        assertEquals(25, memory.get("x"));

        memory = interpreter.run("incr x; init x = 4761;");
        assertEquals(4761, memory.get("x"));
    }

    public void testWhile() throws NotAssignedException {
        Interpreter interpreter = new Interpreter();
        Memory memory = interpreter.run("init x = 25; clear y; while x not 0 do; incr y; decr x; end;");
        assertEquals(25, memory.get("y"));
    }
}