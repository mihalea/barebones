package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.ExpectedNumberException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidNamingException;
import ro.mihalea.cadets.barebones.logic.exceptions.NotAssignedException;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Instruction that decrements a list of variables
 * Sytnax: decr variables...
 */
public class Decrement extends BaseInstruction {
    /**
     * List of variables to be decremented
     */
    private List<String> variables = new ArrayList<>();

    /**
     * Decreases the variable by one if set, otherwise sets it to -1
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
                    memory.set(var, memory.get(var) - 1);
                else
                    memory.set(var, -1);
            } catch (NotAssignedException e) {
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
    public BaseInstruction decode(LinkedList<String> args) throws InvalidNamingException {
        for(String arg : args) {
            if(!Pattern.matches(REGEX_NAME, arg))
                throw new InvalidNamingException(arg);
            variables.add(arg);
        }
        return this;
    }
}
