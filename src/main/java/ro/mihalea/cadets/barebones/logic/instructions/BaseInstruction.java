package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.ExpectedNumberException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidNamingException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidSyntaxException;
import ro.mihalea.cadets.barebones.logic.exceptions.NotAssignedException;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * Base model for all the decoded instructions
 */
public abstract class BaseInstruction {
    /**
     * Regex used for determining whether a variable name is valid or not
     */
    protected String REGEX_NAME = "[a-zA-Z_][a-zA-Z0-9_]*";

    /**
     * Regex used in determining whether an argument is a valid number
     */
    protected String REGEX_NUM = "\\d+";

    /**
     * Executes the instruction and returns the next program counter
     * @param programCounter Current program counter
     * @param memory Memory handler
     * @return Next program counter
     */
    public abstract int execute(int programCounter, Memory memory) throws NotAssignedException;

    /**
     * /**
     * Creates the instruction based on the arguments provided
     * @param args Arguments to be handled by the instruction
     * @return Final instruction

     * @throws InvalidSyntaxException Syntax does not follow the specification
     * @throws InvalidNamingException One or more variables do not follow the naming specification
     * @throws ExpectedNumberException Expected a number instead of a variable name
     */
    public abstract BaseInstruction decode(LinkedList<String> args) throws InvalidSyntaxException, InvalidNamingException, ExpectedNumberException;
}
