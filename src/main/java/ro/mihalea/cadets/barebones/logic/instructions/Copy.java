package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.InvalidSyntaxException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidVariableNameException;
import ro.mihalea.cadets.barebones.logic.exceptions.NoValueAssignedException;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

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
    public BaseInstruction decode(LinkedList<String> args) throws InvalidSyntaxException, InvalidVariableNameException {
        dest = new ArrayList<>();
        if(args.size() < 3)
            throw new InvalidSyntaxException(-1);

        source = args.pop();
        if(!Pattern.matches(NAME_REGEX, source))
            throw new InvalidSyntaxException(-1);

        if(!args.pop().equals(">"))
            throw new InvalidSyntaxException(-1);

        for(String arg : args) {
            if (!Pattern.matches(NAME_REGEX, arg))
                throw new InvalidVariableNameException(-1);

            dest.add(arg);
        }

        return null;
    }
}
