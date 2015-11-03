package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.*;
import ro.mihalea.cadets.barebones.logic.units.Evaluator;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Instruction which decides at every iteration based on the terms if it should execute the block inside
 */
public class While extends BlockInstruction {
    /**
     * Condition used to determine if an iteration should run
     */
    private LinkedList<String> expression = new LinkedList<>();

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
    public int execute(int programCounter, Memory memory) throws NotAssignedException, InvalidCharacterException, InvalidExpressionException {
        Evaluator ev = new Evaluator(memory);
        long result = ev.evaluate(expression);

        return result != 0 ? programCounter + 1 : pairIndex + 1;
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
    public BaseInstruction decode(LinkedList<String> args) throws InvalidSyntaxException, InvalidNamingException,
            InvalidCharacterException {
        String token = "";
        while(!args.isEmpty() && !(token = args.remove()).equals("do")) {
            if(!(Evaluator.isVariable(token) || Evaluator.isNumber(token) || Evaluator.isOperator(token)))
                throw new InvalidCharacterException(token);
            expression.add(token);
        }

        if(!token.equals("do"))
            throw new InvalidSyntaxException("Expected do at the end");

        return this;
    }
}
