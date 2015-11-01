package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.NoValueAssignedException;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Instruction that increments a list of variables
 */
public class Increment implements InstructionInterface {
    /**
     * List of variables to be incremented
     */
    private List<String> variables;


    /**
     * Increases the variable by one if set, otherwise sets it to 1
     * and return the next consecutive program counter
     * @param programCounter Current program counter
     * @param memory Memory handler
     * @return Next consecutive program counter
     */
    @Override
    public int execute(int programCounter, Memory memory) {
        for(String var : variables) {
            try {
                if (memory.exists(var))
                    memory.set(var, memory.get(var) + 1);
                else
                    memory.set(var, 1);
            } catch (NoValueAssignedException e) {
                e.printStackTrace();
            }
        }

        return programCounter + 1;
    }

    /**
     * Assumes all the arguments parsed are variable names
     * @param args Arguments to be handled by the instruction
     * @return The same object
     */
    @Override
    public InstructionInterface decode(Stack<String> args) {
        variables = new ArrayList<>(args);
        return this;
    }
}
