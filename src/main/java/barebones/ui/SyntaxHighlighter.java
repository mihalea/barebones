package barebones.ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mircea on 02/10/15.
 */
public class SyntaxHighlighter implements DocumentListener {
    private final List<String> INSTRUCTIONS;

    public SyntaxHighlighter() {
        INSTRUCTIONS = new ArrayList<String>() {{
            add("incr");
            add("clear");
            add("decr");
            add("while");
        }};
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        do_magic(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        do_magic(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        do_magic(e);
    }

    private void do_magic(DocumentEvent e) {
        try {
            Document doc = e.getDocument();
            String text = doc.getText(0, doc.getLength());
            int start, end;
            start = end = e.getOffset();

            String delimiters = " \\n";

            while(start > 0 && delimiters.indexOf(text.charAt(start-1)) == -1)
                start--;

            while(end < doc.getLength() - 1 && delimiters.indexOf(text.charAt(end + 1)) == -1)
                end++;

            String word = text.substring(start, end+1);
            String high = "<strong>" + word + "</strong>";

        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }
}
