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
}