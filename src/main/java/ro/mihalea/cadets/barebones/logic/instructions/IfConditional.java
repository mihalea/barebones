package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.*;
import ro.mihalea.cadets.barebones.logic.units.Evaluator;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if(expression.size() == 0)
            return programCounter + 1;
        else {
            Evaluator ev = new Evaluator(memory);
            if(ev.evaluate(expression) != 0)
                return programCounter + 1;
            else
                return pairIndex;
        }
    }

    @Override
    public BaseInstruction decode(LinkedList<String> args) throws InvalidSyntaxException,
            InvalidNamingException, ExpectedNumberException, InvalidCharacterException {
        Pattern pattern = Pattern.compile("pair=([0-9]+)");
        Matcher matcher = pattern.matcher(args.peek());
        if(matcher.find()) {
            this.setPairIndex(Integer.parseInt(matcher.group(1)));
            args.pop();
        }


        for(String token : args) {
            if(!(Evaluator.isVariable(token) || Evaluator.isOperator(token) || Evaluator.isNumber(token)))
                throw new InvalidCharacterException(token);
            expression.add(token);
        }

        return this;
    }
}
