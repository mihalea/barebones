package ro.mihalea.cadets.barebones.logic;

import junit.framework.TestCase;

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
}