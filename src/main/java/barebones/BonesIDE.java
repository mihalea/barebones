package barebones;

import barebones.logic.Interpreter;
import barebones.logic.Listener;
import barebones.ui.BonesFrame;

/**
 * Created by mircea on 02/10/15.
 */
public class BonesIDE {
    private BonesFrame frame;
    private Interpreter interpreter;
    private Listener listener;

    public void start() {
        interpreter = new Interpreter();
        listener = interpreter.setupListener();
        frame = new BonesFrame();
        frame.addBarebonesListener(listener);
    }
}
