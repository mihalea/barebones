package ro.mihalea.cadets.barebones.logic.units;

import ro.mihalea.cadets.barebones.logic.Line;
import ro.mihalea.cadets.barebones.logic.exceptions.BonesException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidCharacterException;
import ro.mihalea.cadets.barebones.logic.exceptions.NotTerminatedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that handles the raw instruction buffer
 */
public class Fetcher {
    /**
     * Instruction buffer waiting to be send to the {@link Decoder}
     */
    private List<Line> instructions;

    /**
     * Counts how many lines have been added
     */
    private int lineCount;

    /**
     * Instantiates the field
     */
    public Fetcher() {
        instructions = new ArrayList<>();
        lineCount = 0;
    }

    /**
     * Adds one or more lines to the raw instruction buffer
     * @param code One or more lines of code
     */
    public void add(String code) throws NotTerminatedException, InvalidCharacterException{
        String[] statements = code.split(";");

        /**
         * If the last non-whitespace character in the String
         * is not a semicolon then we can be sure that at least
         * one statement has not been correctly terminated
         */
        if(code.trim().charAt(code.length() - 1) != ';')
            throw new NotTerminatedException(lineCount);
        for (String s : statements) {
            lineCount++;
            if (!s.isEmpty()) {
                /**
                 * Sanitize the line by removing unnseccessary whitespace
                 * and testing against invalid characters
                 */
                String sanitized = String.join(" ", s.trim().split("\\W"));
                Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\W]");
                Matcher matcher = pattern.matcher(sanitized);

                if(matcher.matches())
                    throw new InvalidCharacterException(lineCount);

                instructions.add(new Line(s.trim(), lineCount++));
            }
        }
    }

    /**
     * Returns the instruction buffer and clears it
     * @return Raw instruction buffer
     */
    public List<Line> fetch() {
        List<Line> returned = new ArrayList<>(instructions);
        instructions.clear();
        return returned;
    }

    /**
     * Clears the current instruction buffer
     */
    public void clear() {
        instructions.clear();
        lineCount = 0;
    }
}
