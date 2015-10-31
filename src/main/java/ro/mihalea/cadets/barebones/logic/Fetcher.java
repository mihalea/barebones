package ro.mihalea.cadets.barebones.logic;

import javafx.util.Pair;
import ro.mihalea.cadets.barebones.logic.exceptions.NotTerminatedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles the raw instruction buffer
 */
public class Fetcher {
    /**
     * Instruction buffer waiting to be send to the {@link Decoder}
     */
    private List<String> instructions;

    /**
     * Counts how many lines have been added
     */
    private int line;

    /**
     * Instantiates the field
     */
    public Fetcher() {
        instructions = new ArrayList<>();
        line = 0;
    }

    /**
     * Adds one or more lines to the raw instruction buffer
     * @param code One or more lines of code
     */
    public void add(String code) throws NotTerminatedException{
        String[] statements = code.split(";");

        /**
         * If the last non-whitespace character in the String
         * is not a semicolon then we can be sure that at least
         * one statement has not been correctly terminated
         */
        if(code.trim().charAt(code.length() - 1) != ';')
            throw new NotTerminatedException(line);
        for (String s : statements) {
            line++;
            if (!s.isEmpty())
                //Trim surrounding whitespace
                instructions.add(s.trim());
        }
    }

    /**
     * Returns the instruction buffer and clears it
     * @return Raw instruction buffer
     */
    public List<String> fetch() {
        List<String> returned = new ArrayList<>(instructions);
        instructions.clear();
        return returned;
    }

    /**
     * Clears the current instruction buffer
     */
    public void clear() {
        instructions.clear();
    }
}
