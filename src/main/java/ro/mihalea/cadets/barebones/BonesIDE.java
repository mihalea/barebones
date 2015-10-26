package ro.mihalea.cadets.barebones;

import ro.mihalea.cadets.barebones.logic.Interpreter;
import ro.mihalea.cadets.barebones.logic.Listener;
import ro.mihalea.cadets.barebones.ui.BonesFrame;

/**
 * Controller object which combines the backend and the frontend.
 */
public class BonesIDE {
    private BonesFrame frame;
    private Interpreter interpreter;
    private Listener listener;

    /**
     * Must be called to display the IDE.
     * It initializes all of it's values here.
     */
    public void start() {
        interpreter = new Interpreter();
        listener = interpreter.setupListener();
        frame = new BonesFrame();
        frame.addBarebonesListener(listener);
    }
}
