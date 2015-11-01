package ro.mihalea.cadets.barebones;

import ro.mihalea.cadets.barebones.logic.Interpreter;
import ro.mihalea.cadets.barebones.logic.Listener;
import ro.mihalea.cadets.barebones.ui.BonesFrame;

/**
 * Controller object which combines the backend and the frontend.
 */
public class BonesIDE {

    /**
     * Must be called to display the IDE.
     * It initializes all of it's values here.
     */
    public void start() {
        Interpreter interpreter = new Interpreter();
        Listener listener = interpreter.setupListener();
        BonesFrame frame = new BonesFrame();
        frame.addBarebonesListener(listener);
    }
}
