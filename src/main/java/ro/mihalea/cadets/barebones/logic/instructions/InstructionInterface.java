package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.Stack;

/**
 * Base model for all the decoded instructions
 */
public interface InstructionInterface {
    void execute(Memory memory);
    InstructionInterface decode(Stack<String> args);
}
