package ro.mihalea.cadets.barebones.logic.units;

import ro.mihalea.cadets.barebones.logic.exceptions.*;
import ro.mihalea.cadets.barebones.logic.instructions.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles the creation of new instructions based on raw input
 */
public class Decoder {
    /**
     * Regex used for testing against invalid characters
     */
    private final String INVALID_REGEX = "[^a-zA-Z0-9\\s]";

    /**
     * Decoded instruction buffer waiting to be sent to {@link Processor}
     */
    private final List<BaseInstruction> instructions;

    /**
     * Instantiates the fields
     */
    public Decoder() {
        instructions = new ArrayList<>();
    }

    /**
     * Appends one or more lines of instructions to the current buffer
     * @param code One or more instructions
     */
    public void append(String code) throws BlockUnfinishedException, InvalidCharacterException,
            UnknownInstructionException, InvalidSyntaxException, InvalidNamingException {
        String[] statements = code.split(";");
        int lineCount = 0;

        /**
         * If the last non-whitespace character in the String
         * is not a semicolon then we can be sure that at least
         * one statement has not been correctly terminated
         */
        if(code.trim().charAt(code.length() - 1) != ';')
            throw new BlockUnfinishedException(statements.length);
        else
            for (String statement : statements) {
                lineCount++;
                if (!statement.isEmpty()) {
                    /**
                     * Sanitize the line by removing unnecessary whitespace
                     * and testing against invalid characters
                     */
                    String sanitized = String.join(" ", statement.trim().split("\\s"));
                    Pattern pattern = Pattern.compile(INVALID_REGEX);
                    Matcher matcher = pattern.matcher(sanitized);
                    if(matcher.find())
                        throw new InvalidCharacterException(lineCount);

                    try {
                        instructions.add(this.decode(sanitized, lineCount));
                    } catch (UnknownInstructionException|InvalidSyntaxException e) {
                        e.setLine(lineCount);
                        throw e;
                    }
                }
            }

    }

    /**
     * Decodes one instruction and creates it's corresponding {@link BaseInstruction}
     * @param rawInstruction One raw instruction
     * @return Matching {@link BaseInstruction} implementation
     */
    private BaseInstruction decode(final String rawInstruction, final int lineIndex)
            throws UnknownInstructionException, InvalidSyntaxException, InvalidNamingException {
        /**
         * Adds all the tokes to a LinkedList (which implements Queue)
         */
        LinkedList<String> arguments = new LinkedList<>();
        arguments.addAll(Arrays.asList(rawInstruction.split("\\W")));


        /**
         * First token is the instruction code
         */
        String instruction = arguments.pop();
        switch(instruction) {
            case "incr":
                return new Increment().decode(arguments);
            case "decr":
                return new Decrement().decode(arguments);
            case "clear":
                return new Clear().decode(arguments);
            case "copy":
                return new Copy().decode(arguments);
            case "init":
                break;
            case "while":
                break;
            case "end":
                break;
            default:
                throw new UnknownInstructionException(lineIndex);
        }

        //!! TO BE REMOVED AFTER THE SWITCH IS DONE !!
        return null;
    }

    /**
     * Signals whether all the block instructions have been closed and it
     * is safe to proceed to the Processor with the current instruction batch
     * @return Can proceed with fetch
     */
    public boolean canFetch() {
        return true;
    }

    public List<BaseInstruction> fetch() throws BlockUnfinishedException {
        if(!canFetch())
            throw new BlockUnfinishedException();
        List<BaseInstruction> returned = new ArrayList<>(instructions);
        instructions.clear();
        return returned;
    }
}
