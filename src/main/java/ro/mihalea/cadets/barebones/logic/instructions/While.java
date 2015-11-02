package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.ExpectedNumberException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidNamingException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidSyntaxException;
import ro.mihalea.cadets.barebones.logic.exceptions.NotAssignedException;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * Instruction which decides at every iteration based on the terms if it should execute the block inside
 */
public class While extends BlockInstruction {
    /**
     * Left term of the while is expected to be a variable
     */
    private String leftTerm;

    /**
     * Right term of the while is expected to be a variable or a number
     */
    private String rightTerm;

    public While(int lineIndex) {
        super(lineIndex);
    }

    /**
     * Retrieves the value of both terms and then depending on the comparison it decides whether
     * to do one more iteration
     * @param programCounter Current program counter
     * @param memory Memory handler
     * @return The next consecutive program counter, if the variables are the same, or the next one after
     * the matching end instruction if they differ
     * @throws NotAssignedException
     */
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

    /**
     * The first argument is expected to be a variable name, while the second one may be a variable name
     * or a whole number
     * @param args Arguments to be handled by the instruction
     * @return Same instruction
     * @throws InvalidSyntaxException
     * @throws InvalidNamingException
     */
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
