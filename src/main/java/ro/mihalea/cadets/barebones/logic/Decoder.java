package ro.mihalea.cadets.barebones.logic;

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
     * Instantiates the fields
     */
    public Decoder() {
        instructions = new ArrayList<>();
    }

    /**
     * Transforms the array of raw instructions to a list
     * of decoded instructions
     * @param rawInstructions Array of raw instructions
     */
    public void decode(String[] rawInstructions) {
        for (String raw : rawInstructions)
            instructions.add(this.decode(raw));
    }

    /**
     * Decodes one instruction and creates it's corresponding InstructionInterface
     * @param rawInstruction One raw instruction
     * @return Matchind InstructionInterface implementation
     */
    private InstructionInterface decode(String rawInstruction) {
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
                break;
        }

        return null;
    }
}
