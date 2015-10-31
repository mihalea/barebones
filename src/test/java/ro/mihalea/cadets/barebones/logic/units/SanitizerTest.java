package ro.mihalea.cadets.barebones.logic.units;

import junit.framework.TestCase;
import ro.mihalea.cadets.barebones.logic.exceptions.BonesException;
import ro.mihalea.cadets.barebones.logic.exceptions.InvalidCharacterException;
import ro.mihalea.cadets.barebones.logic.exceptions.NotTerminatedException;

import java.util.List;

/**
 * Created by Mircea on 31-Oct-15.
 */
public class SanitizerTest extends TestCase {

    public void testAddSingleStatement() throws Exception {
        Sanitizer sanitizer = new Sanitizer();
        assertNotNull(sanitizer);

        assertTrue(sanitizer.add("incr x;"));
        List<Line> instr = sanitizer.fetch();
        assertEquals(1, instr.size());
        assertTrue(instr.get(0).equals("incr x"));

        instr = sanitizer.fetch();
        assertEquals(0, instr.size());
    }

    public void testSameLineStatements() throws Exception {
        Sanitizer sanitizer = new Sanitizer();
        assertNotNull(sanitizer);

        assertTrue(sanitizer.add("incr x; decr x;"));
        List<Line> instr = sanitizer.fetch();
        assertEquals(2, instr.size());
        assertTrue(instr.get(0).equals("incr x"));
        assertTrue(instr.get(1).equals("decr x"));

        instr = sanitizer.fetch();
        assertEquals(0, instr.size());
    }

    public void testMultipleLines() throws Exception {
        Sanitizer sanitizer = new Sanitizer();
        assertNotNull(sanitizer);

        assertTrue(sanitizer.add("incr x;"));
        assertTrue(sanitizer.add("decr x; clear y;"));
        List<Line> instr = sanitizer.fetch();
        assertEquals(3, instr.size());
        assertTrue(instr.get(0).equals("incr x"));
        assertTrue(instr.get(1).equals("decr x"));
        assertTrue(instr.get(2).equals("clear y"));

        instr = sanitizer.fetch();
        assertEquals(0, instr.size());
    }

    public void testClearAfterAdd() throws Exception {
        Sanitizer sanitizer = new Sanitizer();
        assertNotNull(sanitizer);

        assertTrue(sanitizer.add("incr x; decr x;"));
        sanitizer.clear();
        List<Line> instr = sanitizer.fetch();
        assertEquals(0, instr.size());
    }

    public void testAddAfterClear() throws Exception {
        Sanitizer sanitizer = new Sanitizer();
        assertNotNull(sanitizer);

        sanitizer.clear();
        assertTrue(sanitizer.add("decr x;"));
        List<Line> instr = sanitizer.fetch();
        assertEquals(1, instr.size());
        assertTrue(instr.get(0).equals("decr x"));
    }

    public void testClearOnEmpty() throws Exception {
        Sanitizer sanitizer = new Sanitizer();
        assertNotNull(sanitizer);

        sanitizer.clear();
        assertEquals(0, sanitizer.fetch().size());
    }

    public void testNotTerminated() throws Exception {
        Sanitizer sanitizer = new Sanitizer();
        assertNotNull(sanitizer);

        assertTrue(sanitizer.add("incr x;"));
        assertFalse(sanitizer.add("decr y"));
        assertFalse(sanitizer.add("decr x; incr y"));

        List<BonesException> exceptions = sanitizer.getExceptions();
        for (BonesException exception : exceptions)
            if(!(exception instanceof NotTerminatedException))
                fail("Exception thrown not of expected type");
    }

    public void testInvalidCharacter() throws Exception {
        Sanitizer sanitizer = new Sanitizer();
        assertNotNull(sanitizer);

        assertTrue(sanitizer.add("incr x; decr x2;"));
        assertFalse(sanitizer.add("incr sf#;"));

        List<BonesException> exceptions = sanitizer.getExceptions();
        for (BonesException exception : exceptions)
            if(!(exception instanceof InvalidCharacterException))
                fail("Exception thrown not of expected type");
    }
}