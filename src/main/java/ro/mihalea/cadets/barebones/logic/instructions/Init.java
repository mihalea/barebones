package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.ExpectedNumberException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidNamingException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidSyntaxException;
import ro.mihalea.cadets.barebones.logic.exceptions.NotAssignedException;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * Instruction that sets a single variable to a value
 * Syntax: init [variable] = [value]
 */
public class Init extends BaseInstruction {
    /**
     * Variable to be set
     */
    private String variable;

    /**
     * Value which will be put into the variable
     */
    private long value;

    public Init(int lineIndex) {
        super(lineIndex);
    }

    /**
     * Sets the variable to the provided value
     * @param programCounter Current program counter
     * @param memory Memory handler
     * @return The next consecutive program counter
     */
    @Override
    public int execute(int programCounter, Memory memory)  {
        memory.set(variable, value);
        return programCounter + 1;
    }

    @Override
    public BaseInstruction decode(LinkedList<String> args) throws InvalidSyntaxException, InvalidNamingException,
            ExpectedNumberException {
        if(args.size() != 3)
            throw new InvalidSyntaxException();

        variable = args.pop();
        if(!Pattern.matches(REGEX_NAME, variable))
            throw new InvalidNamingException(variable);

        if(!args.pop().equals("="))
            throw new InvalidSyntaxException();

        String rawValue = args.pop();
        if(!Pattern.matches(REGEX_NUM, rawValue))
            throw new ExpectedNumberException(rawValue);

        value = Long.parseLong(rawValue);
        return this;
    }
}
