package ro.mihalea.cadets.barebones.ui;


import ro.mihalea.cadets.barebones.events.ErrorResponse;
import ro.mihalea.cadets.barebones.events.EventResponse;
import ro.mihalea.cadets.barebones.logic.Listener;
import ro.mihalea.cadets.barebones.events.ResultResponse;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;
import ro.mihalea.cadets.barebones.logic.exceptions.BonesException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.List;

/**
 * Panel containing all the swing components used to build the GUI
 */
public class BonesPanel extends JPanel {

    /**
     * Frame which created this panel
     */
    private final JFrame parent;

    /**
     * List of listeners listening to GUI events
     */
    private final List<Listener> listeners;

    /**
     * Panel containing the {@link this#dataModel}
     */
    private JScrollPane debugPane;

    /**
     * Data model which holds all the variables and their values after execution
     */
    private DefaultTableModel dataModel;

    /**
     * Main component used to create and edit code
     */
    private RSyntaxTextArea textArea;

    /**
     * Text area of the panel containing the interpreter output
     */
    private JTextArea messageArea;

    /**
     * ScrollPane used to contain the messageArea
     */
    private JScrollPane messagePane;

    /**
     * Creates a new JPanel containing a parent JFrame a list of listeners
     * @param parent Parent JFrame containing this
     * @param barebonesListeners List of listeners
     */
    public BonesPanel(JFrame parent, java.util.List<Listener> barebonesListeners) {
        this.parent = parent;
        this.listeners = barebonesListeners;

        this.setupPanel();
    }

    /**
     * Creates the different panels and initiates them
     */
    private void setupPanel() {

        this.setLayout(new BorderLayout());
        //Padding on the window
        this.setBorder(new EmptyBorder(5, 15, 20, 15));

        setupMenuBar();
        setupButtonPanel();
        setupEditPanel();
        setupMessagePanel();
    }

    /**
     * Sets up the area containing the messages from the interpreter
     */
    private void setupMessagePanel() {
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setRows(3);
        messagePane = new JScrollPane(messageArea);

        this.add(messagePane, BorderLayout.PAGE_END);
    }

    /**
     * Sets up the area containing the buttons and places it at the top of the page
     */
    private void setupButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));

        JButton run = new JButton("Run");
        run.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Gets the code as a continuous string
                String code = textArea.getText();
                //Clears the table of variable from the previous iterations
                dataModel.setRowCount(0);

                //Alerts all the listeners that the user is trying to compile
                for (Listener li : listeners) {
                    EventResponse response = li.interpret(code);
                    //Gets the response
                    StringBuilder output = new StringBuilder();

                    //it compiled successfully
                    if(response instanceof ResultResponse) {
                        //Adds the variable values to the model behind the GUI
                        ResultResponse result = (ResultResponse) response;
                        for (Map.Entry<String, Long> kv : result.memory.getVariables().entrySet()) {
                            dataModel.addRow(new Object[]{kv.getKey(), kv.getValue()});
                        }

                        messageArea.setForeground(Color.BLACK);
                        output.append("Compilation successful!\n");
                    //It failed to compile
                    } else if(response instanceof ErrorResponse) {
                        //Adds the error to the compiler output
                        BonesException ex = ((ErrorResponse) response).exception;
                        output.append("Error caught ");
                        if(ex.getLine() != -1)
                            output.append("on line ").append(ex.getLine());
                        output.append(": ").append(ex.getMessage());

                        messageArea.setForeground(Color.RED);



                    }

                    //Appends memory usage and cpu time to the console output
                    output.append("Memory used: ").append((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
                            / 1024 / 1024).append(" MB\n");
                    output.append("CPU time: ").append(response.timeElapsed).append(" ms");

                    messageArea.setText(output.toString());
                }
            }
        });
        panel.add(run);

        JButton debug = new JButton("Debug");
        panel.add(debug);

        this.add(panel, BorderLayout.PAGE_START);
    }

    /**
     * Sets up the text editor with syntax highlighting
     */
    private void setupEditPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        /**
         * Creates a new RSyntaxTextArea and adds a custom token maker to it built specifically for gradle
         */
        textArea = new RSyntaxTextArea();
        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("text/barebones", "ro.mihalea.cadets.barebones.ui.BonesSyntax");
        textArea.setSyntaxEditingStyle("text/barebones");
        RTextScrollPane scrollPane = new RTextScrollPane(textArea);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.8d;
        c.weighty = 1d;
        c.fill = GridBagConstraints.BOTH;
        centerPanel.add(scrollPane, c);

        /**
         * Creates the table that holds all the variable with their values after a successful execution
         */
        dataModel = new DefaultTableModel();
        dataModel.addColumn("Variable");
        dataModel.addColumn("Value");
        JTable debugTable = new JTable(dataModel);
        debugTable.getColumnModel().getColumn(0).setWidth(10);
        debugTable.getColumnModel().getColumn(1).setWidth(10);
        debugPane = new JScrollPane(debugTable);
        debugPane.setPreferredSize(new Dimension(100, 0));

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.2d;
        c.weighty = 1d;
        centerPanel.add(debugPane, c);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Sets up the menu bar at the top of the window
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");

        JMenuItem file_new = new JMenuItem("New");
        file.add(file_new);

        JMenuItem file_open = new JMenuItem("Open");
        file.add(file_open);

        menuBar.add(file);

        parent.setJMenuBar(menuBar);
    }
}