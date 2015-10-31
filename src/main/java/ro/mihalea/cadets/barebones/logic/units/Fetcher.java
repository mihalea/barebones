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
    private final String INVALID_REGEX = "^[a-zA-Z0-9\\W]";

    /**
     * Instruction buffer waiting to be send to the {@link Decoder}
     */
    private List<Line> instructions;

    /**
     * Counts how many lines have been added
     */
    private int lineCount;

    /**
     * Exceptions thrown for the current lines in the buffer
     */
    private List<BonesException> exceptions;

    /**
     * Instantiates the field
     */
    public Fetcher() {
        instructions = new ArrayList<>();
        exceptions = new ArrayList<>();
        lineCount = 0;
    }

    /**
     * Adds one or more lines to the raw instruction buffer
     * @param code One or more lines of code
     * @return Returns true if no more exceptions got caught during sanitation
     */
    public boolean add(String code) throws NotTerminatedException, InvalidCharacterException {
        String[] statements = code.split(";");
        final int initialExceptions = exceptions.size();

        /**
         * If the last non-whitespace character in the String
         * is not a semicolon then we can be sure that at least
         * one statement has not been correctly terminated
         */
        if(code.trim().charAt(code.length() - 1) != ';')
            exceptions.add(new NotTerminatedException(lineCount));
        else
            for (String s : statements) {
                lineCount++;
                if (!s.isEmpty()) {
                    /**
                     * Sanitize the line by removing unnecessary whitespace
                     * and testing against invalid characters
                     */
                    String sanitized = String.join(" ", s.trim().split("\\W"));
                    Pattern pattern = Pattern.compile(INVALID_REGEX);
                    Matcher matcher = pattern.matcher(sanitized);

                    if(matcher.matches())
                        exceptions.add(new InvalidCharacterException(lineCount));
                    else
                        instructions.add(new Line(s.trim(), lineCount++));
                }
            }

        return exceptions.size() == initialExceptions;
    }

    /**
     * Returns the instruction buffer and clears it
     * @return Raw instruction buffer
     */
    public List<Line> fetch() {
        List<Line> returned = new ArrayList<>(instructions);
        instructions.clear();
        exceptions.clear();
        return returned;
    }

    /**
     * Clears the current instruction buffer
     */
    public void clear() {
        instructions.clear();
        exceptions.clear();
        lineCount = 0;
    }

    /**
     * @return Returns all the exception that got thrown after the last
     * {@link this#fetch()} call
     */
    public List<BonesException> getExceptions() {
        return exceptions;
    }
}
