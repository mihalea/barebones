package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.ExpectedNumberException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidNamingException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidSyntaxException;
import ro.mihalea.cadets.barebones.logic.exceptions.NotAssignedException;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Instruction that copies the value of one variable to a list of value
 * Syntax: copy [source] to [dest...]
 */
public class Copy extends BaseInstruction {
    /**
     * Variable from which we copy
     */
    String source;

    /**
     * Variables to which we copy
     */
    List<String> dest = new ArrayList<>();

    public Copy(int lineIndex) {
        super(lineIndex);
    }


    /**
     * Sets the value of all the variables in dest to the value of source
     * @param programCounter Current program counter
     * @param memory Memory handler
     * @return Next consectuive instruction
     * @throws NotAssignedException The source variable has not been assigned a value prior to execution
     */
    @Override
    public int execute(int programCounter, Memory memory) throws NotAssignedException {
        long sourceValue = memory.get(source);
        for (String d : dest)
            memory.set(d, sourceValue);
        return programCounter + 1;
    }

    /**
     * Decodes the arguments according to
     * @param args Arguments to be handled by the instruction
     * @return The same method used for chaining
     * @throws InvalidSyntaxException Instruction does not follow the imposed syntax
     * @throws InvalidNamingException One or more variables do not follow the naming specification
     */
    @Override
    public BaseInstruction decode(LinkedList<String> args) throws InvalidSyntaxException, InvalidNamingException {
        if(args.size() < 3)
            throw new InvalidSyntaxException();

        source = args.pop();
        if(!Pattern.matches(REGEX_NAME, source))
            throw new InvalidNamingException(source);

        if(!args.pop().equals("to"))
            throw new InvalidSyntaxException();

        for(String arg : args) {
            if(!Pattern.matches(REGEX_NAME, arg))
                throw new InvalidNamingException(arg);
            dest.add(arg);
        }

        return this;
    }
}
