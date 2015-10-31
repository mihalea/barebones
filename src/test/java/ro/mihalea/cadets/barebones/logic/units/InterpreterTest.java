package ro.mihalea.cadets.barebones.logic.units;

import junit.framework.TestCase;
import ro.mihalea.cadets.barebones.logic.Interpreter;
import ro.mihalea.cadets.barebones.logic.Listener;
import ro.mihalea.cadets.barebones.logic.exceptions.NoValueAssignedException;

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

    public void testIncrement() throws NoValueAssignedException {
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

    public void testDecrement() throws NoValueAssignedException{
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
}