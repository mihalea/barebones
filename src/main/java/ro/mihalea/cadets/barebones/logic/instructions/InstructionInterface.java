package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.Stack;

/**
 * Base model for all the decoded instructions
 */
public interface InstructionInterface {
    /**
     * Executes the instruction and returns the next program counter
     * @param programCounter Current program counter
     * @param memory Memory handler
     * @return Next program counter
     */
    int execute(int programCounter, Memory memory);

    /**
     * Creates the instruction based on the arguments provided
     * @param args Arguments to be handled by the instruction
     * @return Final instruction
     */
    InstructionInterface decode(Stack<String> args);
}
