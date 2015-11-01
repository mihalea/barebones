package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Instruction that sets a list of variables to 0
 * Syntax: clear variables...
 */
public class Clear extends BaseInstruction {
    /**
     * List of variables to be decremented
     */
    private HashSet<String> variables;

    /**
     * Set the variables to zero and return the next consecutive program counter
     * @param programCounter Current program counter
     * @param memory Memory handler
     * @return Next consecutive program counter
     */
    @Override
    public int execute(int programCounter, Memory memory) {
        for(String var : variables) {
                memory.set(var, 0);
        }

        return programCounter + 1;
    }

    /**
     * Assumes all the arguments parsed are variable names
     * @param args Arguments to be handled by the instruction
     * @return The same object
     */
    @Override
    public BaseInstruction decode(LinkedList<String> args) {
        variables = new HashSet<>(args);
        return this;
    }
}
