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
public class While extends BlockInstruction {
    private String leftTerm;
    private String rightTerm;

    @Override
    public int execute(int programCounter, Memory memory) throws NotAssignedException {
        long lValue = memory.get(leftTerm);

        /**
         * Set it to MAX_VALUE to ignore IDE warnings of unset variable.
         * The program should always fit into one of the following ifs, as
         * tested when decoded
         */
        long rValue = Long.MAX_VALUE;
        if(Pattern.matches(REGEX_NAME, rightTerm))
            rValue = memory.get(rightTerm);
        else if(Pattern.matches(REGEX_NUM, rightTerm))
            rValue = Long.parseLong(rightTerm);

        return lValue == rValue ? pairIndex + 1 : programCounter + 1;
    }

    @Override
    public BaseInstruction decode(LinkedList<String> args) throws InvalidSyntaxException, InvalidNamingException {
        leftTerm = args.pop();
        if(!Pattern.matches(REGEX_NAME, leftTerm))
            throw new InvalidNamingException(leftTerm);

        if(!args.pop().equals("not"))
            throw new InvalidSyntaxException("Expected not");

        rightTerm = args.pop();

        if(!Pattern.matches(REGEX_NAME, rightTerm) && !Pattern.matches(REGEX_NUM, rightTerm))
            throw new InvalidSyntaxException("Right term is not a variable nor a number");

        if(!args.pop().equals("do"))
            throw new InvalidSyntaxException("Expected do");

        return this;
    }
}
