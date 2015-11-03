package ro.mihalea.cadets.barebones.ui;


import ro.mihalea.cadets.barebones.Listener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Frame that holds the window configuration and the panel, with a list of listeners
 */
public class BonesFrame extends JFrame {
    /**
     * Initial window width
     */
    private final int WIDTH = 800;

    /**
     * Initial window height
     */
    private final int HEIGHT = 600;

    /**
     * List of listeners for the GUI thread
     */
    private final List<Listener> listeners;

    /**
     * Initiates the window and sets all the settings
     * @throws HeadlessException IfConditional the environment is without a monitor
     */
    public BonesFrame() throws HeadlessException {
        /**
         * Set the title of window
         */
        super("Barebones IDE");

        listeners = new ArrayList<>();

        this.setContentPane(new BonesPanel(this, listeners));

        this.pack();
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //Center the window by setting to relative location
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Add more listeners to the local list
     * @param listener Listener object
     */
    public void addBarebonesListener(Listener listener) {
        this.listeners.add(listener);
    }
}
