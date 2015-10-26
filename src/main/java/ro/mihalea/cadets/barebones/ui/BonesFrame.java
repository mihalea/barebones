package ro.mihalea.cadets.barebones.ui;


import ro.mihalea.cadets.barebones.logic.Listener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mircea on 02/10/15.
 */
public class BonesFrame extends JFrame {
    private final int WIDTH = 800, HEIGHT = 600;
    private final BonesPanel panel;
    private final List<Listener> listeners;

    public BonesFrame() throws HeadlessException {
        super("Barebones IDE");

        listeners = new ArrayList<>();

        this.setContentPane(panel = new BonesPanel(this, listeners));

        this.pack();
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void addBarebonesListener(Listener listener) {
        this.listeners.add(listener);
    }
}
