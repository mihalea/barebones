package ro.mihalea.cadets.barebones.logic.units;

import ro.mihalea.cadets.barebones.logic.Line;
import ro.mihalea.cadets.barebones.logic.exceptions.BonesException;
import ro.mihalea.cadets.barebones.logic.exceptions.UnknownInstructionException;
import ro.mihalea.cadets.barebones.logic.instructions.InstructionInterface;

import java.util.*;

/**
 * Handles the creation of new instructions based on raw input
 */
public class Decoder {
    /**
     * Decoded instruction buffer waiting to be sent to {@link Processor}
     */
    private List<InstructionInterface> instructions;

    /**
     * List of exceptions thrown while trying to decode a chunk of instructions
     */
    private List<BonesException> exceptions;

    /**
     * Instantiates the fields
     */
    public Decoder() {
        instructions = new ArrayList<>();
        exceptions = new ArrayList<>();
    }

    /**
     * Transforms the array of raw instructions to a list
     * of decoded instructions
     * @param rawInstructions Array of raw instructions
     * @return Returns whether the decoder has finished without errors
     */
    public boolean decode(final List<Line> rawInstructions) {
        exceptions.clear();

        for (Line raw : rawInstructions)
            try {
                instructions.add(this.decode(raw));
            } catch (BonesException e) {
                exceptions.add(e);
            }

        return exceptions.size() != 0;
    }

    /**
     * Decodes one instruction and creates it's corresponding InstructionInterface
     * @param rawInstruction One raw instruction
     * @return Matchind InstructionInterface implementation
     */
    private InstructionInterface decode(final Line rawInstruction) throws BonesException {
        /**
         * Adding a reversed list to a stack basically makes
         * it a heap, therefore the confusing naming in the declaration
         */
        Stack<String> heap = new Stack<>();
        List<String> list = Arrays.asList(rawInstruction.getCode().split("\\W"));
        Collections.reverse(list);
        heap.addAll(list);

        /**
         * First token is the instruction code
         */
        switch(heap.peek()) {
            case "incr":
                System.out.println("incr");
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
                throw new UnknownInstructionException(rawInstruction.getIndex());
        }

        return null;
    }
}
