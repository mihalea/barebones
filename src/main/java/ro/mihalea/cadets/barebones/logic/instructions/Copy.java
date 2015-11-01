package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.InvalidSyntaxException;
import ro.mihalea.cadets.barebones.logic.exceptions.NoValueAssignedException;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Instruction that copies the value of one variable to a list of value
 * Syntax: copy source > dest...
 */
public class Copy extends BaseInstruction {
    /**
     * Variable from which we copy
     */
    String source;

    /**
     * Variables to which we copy
     */
    List<String> dest;



    @Override
    public int execute(int programCounter, Memory memory) throws NoValueAssignedException {
        long sourceValue = memory.get(source);
        for (String d : dest)
            memory.set(d, sourceValue);
        return programCounter + 1;
    }

    @Override
    public BaseInstruction decode(LinkedList<String> args) throws InvalidSyntaxException {
        dest = new ArrayList<>();
        if(args.size() < 3)
            throw new InvalidSyntaxException(-1);

        source = args.pop();
        if(!args.pop().equals(">"))
            throw new InvalidSyntaxException(-1);

        dest.addAll(args);

        return null;
    }
}
