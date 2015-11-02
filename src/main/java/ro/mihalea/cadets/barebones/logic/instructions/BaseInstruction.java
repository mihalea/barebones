package ro.mihalea.cadets.barebones.logic.instructions;

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
    protected String NAME_REGEX = "[a-zA-Z_][a-zA-Z0-9_]+";

    /**
     * Executes the instruction and returns the next program counter
     * @param programCounter Current program counter
     * @param memory Memory handler
     * @return Next program counter
     */
    public abstract int execute(int programCounter, Memory memory) throws NotAssignedException;

    /**
     * Creates the instruction based on the arguments provided
     * @param args Arguments to be handled by the instruction
     * @return Final instruction
     */
    public abstract BaseInstruction decode(LinkedList<String> args) throws InvalidSyntaxException, InvalidNamingException;
}
