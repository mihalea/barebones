package ro.mihalea.cadets.barebones.ui;


import ro.mihalea.cadets.barebones.events.ErrorResponse;
import ro.mihalea.cadets.barebones.events.EventResponse;
import ro.mihalea.cadets.barebones.logic.ErrorCaught;
import ro.mihalea.cadets.barebones.logic.Listener;
import ro.mihalea.cadets.barebones.events.ResultResponse;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.List;

/**
 * Created by mircea on 02/10/15.
 */
public class BonesPanel extends JPanel {

    private final JFrame parent;
    private final List<Listener> listeners;

    private JScrollPane debugPane;
    private DefaultTableModel dataModel;
    private RSyntaxTextArea textArea;
    private JTextArea messageArea;
    private JScrollPane messagePane;

    public BonesPanel(JFrame parent, java.util.List<Listener> barebonesListeners) {
        this.parent = parent;
        this.listeners = barebonesListeners;

        this.setupPanel();
    }

    private void setupPanel() {

        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(5, 15, 20, 15));

        setupMenuBar();
        setupButtonPanel();
        setupEditPanel();
        setupMessagePanel();
    }

    private void setupMessagePanel() {
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setRows(3);
        messagePane = new JScrollPane(messageArea);

        this.add(messagePane, BorderLayout.PAGE_END);
    }

    private void setupButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));

        JButton run = new JButton("Run");
        run.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = textArea.getText();
                dataModel.setRowCount(0);

                for (Listener li : listeners) {
                    EventResponse response = li.interpret(code);
                    StringBuffer output = new StringBuffer();

                    if(response instanceof ResultResponse) {
                        ResultResponse result = (ResultResponse) response;
                        for (Map.Entry<String, Long> kv : result.vars.entrySet()) {
                            dataModel.addRow(new Object[]{kv.getKey(), kv.getValue()});
                        }

                        messageArea.setForeground(Color.BLACK);
                        output.append("Compilation successful!\n");
                    } else if(response instanceof ErrorResponse) {
                        ErrorCaught err = ((ErrorResponse) response).error;
                        output.append("Error caught " + (err.line!=-1 ? "on line " + err.line : "") +
                                ": " + err.error.getMessage() + " [code " + err.error.getCode() + "]\n");

                        messageArea.setForeground(Color.RED);



                    }
                    output.append("Memory used: " +
                            (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
                                    / 1024 / 1024 + " MB\n");
                    output.append("CPU time: " + response.timeElapsed + " ms");

                    messageArea.setText(output.toString());
                    if(response.eventConsumed)
                        break;
                }
            }
        });
        panel.add(run);

        JButton debug = new JButton("Debug");
        panel.add(debug);

        this.add(panel, BorderLayout.PAGE_START);
    }

    private void setupEditPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        textArea = new RSyntaxTextArea();
        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("text/barebones", "BonesTokens");
        textArea.setSyntaxEditingStyle("text/barebones");
        RTextScrollPane scrollPane = new RTextScrollPane(textArea);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.8d;
        c.weighty = 1d;
        c.fill = GridBagConstraints.BOTH;
        centerPanel.add(scrollPane, c);

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