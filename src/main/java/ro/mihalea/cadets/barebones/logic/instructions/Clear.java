package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.InvalidNamingException;
import ro.mihalea.cadets.barebones.logic.units.Evaluator;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Instruction that sets a list of variables to 0
 * Syntax: clear [variables...]
 */
public class Clear extends BaseInstruction {
    /**
     * List of variables to be cleared
     */
    private HashSet<String> variables = new HashSet<>();

    public Clear(int lineIndex) {
        super(lineIndex);
    }

    /**
     * Set the variables to zero and return the next consecutive program counter
     * @param programCounter Current program counter
     * @param memory Memory handler
     * @return Next consecutive program counter
     */
    @Override
    public int execute(int programCounter, Memory memory) {
        for(String var : variables)
                memory.set(var, 0);

        return programCounter + 1;
    }

    /**
     * Assumes all the arguments parsed are variable names
     * @param args Arguments to be handled by the instruction
     * @return The same object used for chaining methods
     * @throws InvalidNamingException Variables do not follow the naming specification
     */
    @Override
    public BaseInstruction decode(LinkedList<String> args) throws InvalidNamingException {
        for(String arg : args) {
            if(!Evaluator.isVariable(arg))
                throw new InvalidNamingException(arg);
            variables.add(arg);
        }
        return this;
    }
}
