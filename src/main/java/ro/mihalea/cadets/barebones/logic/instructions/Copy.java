package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.*;
import ro.mihalea.cadets.barebones.logic.units.Evaluator;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.LinkedList;

/**
 * Instruction that copies the value of one variable to a list of value
 * Syntax: copy [expr] to [dest]
 */
public class Copy extends BaseInstruction {
    /**
     * Expression which will be evaluated at runtime
     */
    LinkedList<String> expression = new LinkedList<>();

    /**
     * Variable which will receive the result of the expression
     */
    String dest;

    public Copy(int lineIndex) {
        super(lineIndex);
    }


    /**
     * Sets the value of all the variables in dest to the value of source
     * @param programCounter Current program counter
     * @param memory Memory handler
     * @return Next consecutive instruction
     * @throws NotAssignedException The source variable has not been assigned a value prior to execution
     */
    @Override
    public int execute(int programCounter, Memory memory) throws NotAssignedException,
            InvalidCharacterException, InvalidExpressionException {
        Evaluator evaluator = new Evaluator(memory);
        long result = evaluator.evaluate(expression);
        memory.set(dest, result);
        return this.proposeCounter(programCounter + 1);
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

        String token;
        while(!(token = args.pop()).equals("to")) {
            if(!(Evaluator.isVariable(token) || Evaluator.isNumber(token) || Evaluator.isOperator(token)))
                throw new InvalidSyntaxException();
            expression.add(token);
        }

        dest = args.remove();

        return this;
    }
}
