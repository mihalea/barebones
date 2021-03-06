package ro.mihalea.cadets.barebones.logic.instructions;

import ro.mihalea.cadets.barebones.logic.exceptions.ExpectedNumberException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidNamingException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidSyntaxException;
import ro.mihalea.cadets.barebones.logic.exceptions.NotAssignedException;
import ro.mihalea.cadets.barebones.logic.units.Memory;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Block Instruction referencing back to the matching one
 * syntax: end
 */
public class End extends BlockInstruction {
    public End(int lineIndex) {
        super(lineIndex);
    }

    @Override
    public int execute(int programCounter, Memory memory) throws NotAssignedException {
        return pairIndex;
    }

    /**
     * Decodes the only argument which is the matching instruction's index
     * @param args Arguments to be handled by the instruction
     * @return Final instruction
     * @throws InvalidSyntaxException The instruction does not follow the correct syntax. This exception
     * should normally never be thrown as this is the only instruction which does not take any user
     * input
     */
    @Override
    public BaseInstruction decode(LinkedList<String> args) throws InvalidSyntaxException {

        /**
         * The only argument must be the index of the matching instruction
         */
        if(args.size() != 1)
            throw new InvalidSyntaxException();

        /**
         * Cactching errors here is not necessary because the only argument
         * is an Integer casted as a String, therefore there shouldn't be any
         * casting issues
         */
        Pattern pattern = Pattern.compile("pair=([0-9]+)");
        Matcher matcher = pattern.matcher(args.peek());
        if(matcher.find()) {
            this.setPairIndex(Integer.parseInt(matcher.group(1)));
            args.pop();
        }

        return this;
    }
}
