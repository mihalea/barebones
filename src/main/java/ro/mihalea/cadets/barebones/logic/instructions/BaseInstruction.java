package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.*;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * Base model for all the decoded instructions
 */
public abstract class BaseInstruction {


    /**
     * Line index as described in the code
     */
    protected int lineIndex;

    public BaseInstruction(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    /**
     * Executes the instruction and returns the next program counter
     * @param programCounter Current program counter
     * @param memory Memory handler
     * @return Next program counter
     */
    public abstract int execute(int programCounter, Memory memory) throws NotAssignedException, InvalidCharacterException, InvalidExpressionException;

    /**
     * /**
     * Creates the instruction based on the arguments provided
     * @param args Arguments to be handled by the instruction
     * @return Final instruction

     * @throws InvalidSyntaxException Syntax does not follow the specification
     * @throws InvalidNamingException One or more variables do not follow the naming specification
     * @throws ExpectedNumberException Expected a number instead of a variable name
     */
    public abstract BaseInstruction decode(LinkedList<String> args) throws InvalidSyntaxException, InvalidNamingException, ExpectedNumberException, InvalidCharacterException;

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }
}
