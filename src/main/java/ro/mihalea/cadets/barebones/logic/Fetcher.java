package ro.mihalea.cadets.barebones.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles the raw instruction buffer
 */
public class Fetcher {
    /**
     * Instruction buffer
     */
    List<String> instructions;

    /**
     * Instantiates the field
     */
    public Fetcher() {
        instructions = new ArrayList<>();
    }

    /**
     * Adds one or more lines to the raw instruction buffer
     * @param code One or more lines of code
     */
    public void add(String code) {
        String[] statements = code.split(";");
        for (String s : statements)
            if(!s.isEmpty())
                instructions.add(s);
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
