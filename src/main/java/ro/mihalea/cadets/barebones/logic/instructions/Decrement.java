package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.NoValueAssignedException;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Mircea on 31-Oct-15.
 */
public class Decrement implements InstructionInterface {
    /**
     * List of variables to be incremented
     */
    private List<String> variables;

    @Override
    public void execute(Memory memory) {
        for(String var : variables) {
            try {
                if (memory.exists(var))
                    memory.set(var, memory.get(var) - 1);
                else
                    memory.set(var, -1);
            } catch (NoValueAssignedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public InstructionInterface decode(Stack<String> args) {
        variables = new ArrayList<>(args);
        return this;
    }
}
