package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.ExpectedNumberException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidNamingException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidSyntaxException;
import ro.mihalea.cadets.barebones.logic.exceptions.NotAssignedException;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * Created by Mircea on 02-Nov-15.
 */
public class Init extends BaseInstruction {
    private String variable;
    private long value;

    @Override
    public int execute(int programCounter, Memory memory) throws NotAssignedException {
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
        if(!Pattern.matches(REGEX_NAME, variable))
            throw new ExpectedNumberException(rawValue);

        value = Long.parseLong(rawValue);
        return this;
    }
}
