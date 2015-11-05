package ro.mihalea.cadets.barebones.ui;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;

/**
 * Panel containing information about the current build
 */
public class BonesAbout extends JDialog {
    public BonesAbout(Frame owner) {
        super(owner);

        setupFrame();
        setupPanel();
    }

    private void setupFrame() {
        //this.setSize(new Dimension(300, 200));
    }

    private void setupPanel() {
        JPanel panel = new JPanel();

        JLabel project = new JLabel("Project: BarebonesIDE v1.1");
        JLabel author = new JLabel("Author: Mircea Mihalea <mircea@mihalea.ro>");
        JLabel website = new JLabel("Website: http://github.com/mihalea/barebones");

        panel.add(project);
        panel.add(author);
        panel.add(website);

        this.setContentPane(panel);
    }
}
