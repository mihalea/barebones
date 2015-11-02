package ro.mihalea.cadets.barebones.logic.units;

import junit.framework.TestCase;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidCharacterException;
import ro.mihalea.cadets.barebones.logic.exceptions.BlockUnfinishedException;
import ro.mihalea.cadets.barebones.logic.exceptions.NotTerminatedExpection;
import ro.mihalea.cadets.barebones.logic.instructions.Decrement;
import ro.mihalea.cadets.barebones.logic.instructions.Increment;
import ro.mihalea.cadets.barebones.logic.instructions.BaseInstruction;

import java.util.List;

/**
 * Created by Mircea on 31-Oct-15.
 */
public class DecoderTest extends TestCase {

    public void testAddSingleStatement() throws Exception {
        Decoder decoder = new Decoder();
        assertNotNull(decoder);

        decoder.append("incr x;");
        assertTrue(decoder.canFetch());
        List<BaseInstruction> instr = decoder.fetch();
        assertEquals(1, instr.size());
        assertTrue(instr.get(0) instanceof Increment);

        instr = decoder.fetch();
        assertEquals(0, instr.size());
    }

    public void testSameLineStatements() throws Exception {
        Decoder decoder = new Decoder();
        assertNotNull(decoder);

        decoder.append("incr x; decr x;");
        assertTrue(decoder.canFetch());
        List<BaseInstruction> instr = decoder.fetch();
        assertEquals(2, instr.size());
        assertTrue(instr.get(0) instanceof Increment);
        assertTrue(instr.get(1) instanceof Decrement);

        assertEquals(0, decoder.fetch().size());
    }

    public void testMultipleLines() throws Exception {
        Decoder decoder = new Decoder();
        assertNotNull(decoder);

        decoder.append("incr x;");
        decoder.append("decr x; incr y;");
        assertTrue(decoder.canFetch());
        List<BaseInstruction> instr = decoder.fetch();
        assertEquals(3, instr.size());
        assertTrue(instr.get(0) instanceof Increment);
        assertTrue(instr.get(1) instanceof Decrement);
        assertTrue(instr.get(2) instanceof Increment);

        assertTrue(decoder.canFetch());
        assertEquals(0, decoder.fetch().size());
    }

    public void testNotTerminated() throws Exception {
        Decoder decoder = new Decoder();
        assertNotNull(decoder);

        try {
            decoder.append("incr x;");
            decoder.append("decr y");
            decoder.append("decr x; incr y");
        } catch (NotTerminatedExpection e) {
            System.out.println("Good");
        }
    }

    public void testInvalidCharacter() throws Exception {
        Decoder decoder = new Decoder();
        assertNotNull(decoder);

        try {
            decoder.append("incr x; decr x2;");
            decoder.append("incr sf#;");
        } catch (InvalidCharacterException e) {
            System.out.println("Good");
        }
    }
}