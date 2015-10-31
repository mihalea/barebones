package ro.mihalea.cadets.barebones.logic.units;

import ro.mihalea.cadets.barebones.logic.exceptions.*;
import ro.mihalea.cadets.barebones.logic.instructions.InstructionInterface;

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
    private List<InstructionInterface> instructions;

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
    public void append(String code)throws NotTerminatedException,
            InvalidCharacterException, UnknownInstructionException {
        String[] statements = code.split(";");
        int lineCount = 0;

        /**
         * If the last non-whitespace character in the String
         * is not a semicolon then we can be sure that at least
         * one statement has not been correctly terminated
         */
        if(code.trim().charAt(code.length() - 1) != ';')
            throw new NotTerminatedException(statements.length);
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

                    instructions.add(this.decode(statement, lineCount));
                }
            }

    }

    /**
     * Decodes one instruction and creates it's corresponding {@link InstructionInterface}
     * @param rawInstruction One raw instruction
     * @return Matching {@link InstructionInterface} implementation
     */
    private InstructionInterface decode(final String rawInstruction, final int lineIndex)
            throws UnknownInstructionException {
        /**
         * Adding a reversed list to a stack basically makes
         * it a heap, therefore the confusing naming in the declaration
         */
        Stack<String> heap = new Stack<>();
        List<String> list = Arrays.asList(rawInstruction.split("\\W"));
        Collections.reverse(list);
        heap.addAll(list);

        /**
         * First token is the instruction code
         */
        switch(heap.peek()) {
            case "incr":
                heap.pop();
                break;
            case "decr":
                break;
            case "clear":
                break;
            case "while":
                break;
            case "end":
                break;
            case "copy":
                break;
            case "init":
                break;
            default:
                throw new UnknownInstructionException(lineIndex);
        }

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

    public List<InstructionInterface> fetch() throws BlockNotClosedException {
        if(!canFetch())
            throw new BlockNotClosedException(-1);

        return instructions;
    }
}
