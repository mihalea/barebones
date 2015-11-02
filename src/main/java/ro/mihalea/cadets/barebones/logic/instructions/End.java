package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.ExpectedNumberException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidNamingException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidSyntaxException;
import ro.mihalea.cadets.barebones.logic.exceptions.NotAssignedException;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.LinkedList;

/**
 * Created by Mircea on 02-Nov-15.
 */
public class End extends BlockInstruction {
    @Override
    public int execute(int programCounter, Memory memory) throws NotAssignedException {
        return pairIndex;
    }

    @Override
    public BaseInstruction decode(LinkedList<String> args) throws InvalidSyntaxException, InvalidNamingException, ExpectedNumberException {
        if(args.size() != 1)
            throw new InvalidSyntaxException();

        pairIndex = Integer.parseInt(args.pop());

        return this;
    }
}
