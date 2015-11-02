package ro.mihalea.cadets.barebones.logic.units;

import ro.mihalea.cadets.barebones.logic.exceptions.*;
import ro.mihalea.cadets.barebones.logic.instructions.*;

import java.util.*;

/**
 * Handles the creation of new instructions based on raw input
 */
public class Decoder {
    /**
     * Decoded instruction buffer waiting to be sent to {@link Processor}
     */
    private final List<BaseInstruction> instructions = new ArrayList<>();

    /**
     * Index of instructions starting a block waiting to be paired with an end
     */
    private final Stack<Integer> blocks = new Stack<>();

    /**
     * Indicates whether interpreter is inside a comment and should not try to
     * decode the lines
     */
    private boolean isMultiLineComment = false;


    /**
     * Appends one or more lines of instructions to the current buffer
     * @param code One or more instructions
     */
    public void append(String code) throws BlockUnfinishedException, InvalidCharacterException,
            UnknownInstructionException, InvalidSyntaxException, ExpectedNumberException, NotTerminatedExpection, InvalidNamingException, UnexpectedBlockCloseException {
        int lineCount = 0;

        /**
         * If the last non-whitespace character in the String
         * is not a semicolon then we can be sure that at least
         * one statement has not been correctly terminated
         */
        String[] lines = code.split("\\n");
        for(String line : lines) {
            if(line.isEmpty())
                continue;

            lineCount++;

            line = this.removeComments(line);
            line = line.trim();


            String[] statements = line.split(";");

            for (String statement : statements) {
                if (!statement.isEmpty()) {
                    /**
                     * Sanitize the line by removing unnecessary whitespace
                     * and testing against invalid characters
                     */
                    String sanitized = String.join(" ", statement.trim().split("\\s"));

                    try {
                        BaseInstruction instruction = this.decode(sanitized, lineCount);
                        if(instruction != null) {
                            instructions.add(instruction);
                            if (line.charAt(line.length() - 1) != ';')
                                throw new NotTerminatedExpection(lineCount);
                        }
                    } catch (UnknownInstructionException | InvalidSyntaxException |
                            InvalidNamingException | UnexpectedBlockCloseException e) {
                        e.setLine(lineCount);
                        throw e;
                    }
                }
            }
        }
    }

    /**
     * Decodes one instruction and creates it's corresponding {@link BaseInstruction}
     * @param rawInstruction One raw instruction
     * @return Matching {@link BaseInstruction} implementation
     */
    private BaseInstruction decode(String rawInstruction, final int lineIndex)
            throws InvalidNamingException, InvalidSyntaxException, ExpectedNumberException, UnknownInstructionException, UnexpectedBlockCloseException {

        rawInstruction.trim();
        if(!rawInstruction.isEmpty()) {
            /**
             * Adds all the tokes to a LinkedList (which implements Queue)
             */
            LinkedList<String> arguments = new LinkedList<>();
            arguments.addAll(Arrays.asList(rawInstruction.split("\\s")));

            /**
             * First token is the instruction code
             */
            String instruction = arguments.pop();
            switch (instruction) {
                case "incr":
                    return new Increment().decode(arguments);
                case "decr":
                    return new Decrement().decode(arguments);
                case "clear":
                    return new Clear().decode(arguments);
                case "copy":
                    return new Copy().decode(arguments);
                case "init":
                    return new Init().decode(arguments);
                case "while":
                    blocks.push(instructions.size());
                    return new While().decode(arguments);
                case "end":
                    Integer matchingIndex = blocks.pop();
                    ((BlockInstruction) instructions.get(matchingIndex)).setPairIndex(instructions.size());
                    arguments.push(matchingIndex.toString());
                    return new End().decode(arguments);
                default:
                    throw new UnknownInstructionException(instruction, lineIndex);
            }
        } else {
            return null;
        }
    }

    private String removeComments(String instr) throws UnexpectedBlockCloseException {
        StringBuilder builder = new StringBuilder(instr);
        int start = -1;
        for (int i=0 ; i<builder.length() ; i++) {
            if(builder.charAt(i) == ':' && builder.charAt(i+1) == '#') {
                if(isMultiLineComment) {
                    isMultiLineComment = false;
                    if(start == -1) {
                        builder = builder.delete(0, i + 2);
                        i = 0;
                    }
                    else {
                        builder = builder.delete(start, i + 2);
                        i = start;
                        start = -1;
                    }
                } else {
                    throw new UnexpectedBlockCloseException("Multiline comment not expected to close");
                }

                i++; //Also skip the next character
            } else if(builder.charAt(i) == '#') {
                if(builder.charAt(i+1) == ':') {
                    isMultiLineComment = true;
                    //Also skip the next character
                    start = i++;
                } else {
                    builder = builder.delete(i, builder.length());
                }
            }
        }

        if(isMultiLineComment) {
            if(start != -1)
                builder = builder.delete(start, builder.length());
            else
                builder.delete(0, builder.length());
        }

        return builder.toString();
    }

    /**
     * Signals whether all the block instructions have been closed and it
     * is safe to proceed to the Processor with the current instruction batch
     * @return Can proceed with fetch
     */
    public boolean canFetch() {
        return blocks.empty();
    }

    public List<BaseInstruction> fetch() throws BlockUnfinishedException {
        if(!canFetch())
            throw new BlockUnfinishedException();
        List<BaseInstruction> returned = new ArrayList<>(instructions);
        instructions.clear();
        return returned;
    }
}
