package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.*;
import ro.mihalea.cadets.barebones.logic.units.Evaluator;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.LinkedList;

/**
 * Created by Mircea on 03-Nov-15.
 */
public class IfConditional extends BlockInstruction {
    private LinkedList<String> expression = new LinkedList<>();

    public IfConditional(int lineIndex) {
        super(lineIndex);
    }

    @Override
    public int execute(int programCounter, Memory memory) throws NotAssignedException,
            InvalidCharacterException, InvalidExpressionException {
        return 0;
    }

    @Override
    public BaseInstruction decode(LinkedList<String> args) throws InvalidSyntaxException,
            InvalidNamingException, ExpectedNumberException, InvalidCharacterException {
        for(String token : args) {
            if(!(Evaluator.isNumber(token) || Evaluator.isOperator(token) || Evaluator.isNumber(token)))
                throw new InvalidCharacterException();
            expression.add(token);
        }

        return this;
    }
}
