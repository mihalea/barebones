package ro.mihalea.cadets.barebones.logic.units;

import junit.framework.TestCase;
import ro.mihalea.cadets.barebones.logic.Line;
import ro.mihalea.cadets.barebones.logic.exceptions.BonesException;
import ro.mihalea.cadets.barebones.logic.exceptions.NotTerminatedException;
import ro.mihalea.cadets.barebones.logic.units.Fetcher;

import java.util.List;

/**
 * Created by Mircea on 31-Oct-15.
 */
public class FetcherTest extends TestCase {

    public void testAddSingleStatement() throws Exception {
        Fetcher fetcher = new Fetcher();
        assertNotNull(fetcher);

        assertTrue(fetcher.add("incr x;"));
        List<Line> instr = fetcher.fetch();
        assertEquals(1, instr.size());
        assertTrue(instr.get(0).equals("incr x"));

        instr = fetcher.fetch();
        assertEquals(0, instr.size());
    }

    public void testSameLineStatements() throws Exception {
        Fetcher fetcher = new Fetcher();
        assertNotNull(fetcher);

        assertTrue(fetcher.add("incr x; decr x;"));
        List<Line> instr = fetcher.fetch();
        assertEquals(2, instr.size());
        assertTrue(instr.get(0).equals("incr x"));
        assertTrue(instr.get(1).equals("decr x"));

        instr = fetcher.fetch();
        assertEquals(0, instr.size());
    }

    public void testMultipleLines() throws Exception {
        Fetcher fetcher = new Fetcher();
        assertNotNull(fetcher);

        assertTrue(fetcher.add("incr x;"));
        assertTrue(fetcher.add("decr x; clear y;"));
        List<Line> instr = fetcher.fetch();
        assertEquals(3, instr.size());
        assertTrue(instr.get(0).equals("incr x"));
        assertTrue(instr.get(1).equals("decr x"));
        assertTrue(instr.get(2).equals("clear y"));

        instr = fetcher.fetch();
        assertEquals(0, instr.size());
    }

    public void testClearAfterAdd() throws Exception {
        Fetcher fetcher = new Fetcher();
        assertNotNull(fetcher);

        assertTrue(fetcher.add("incr x; decr x;"));
        fetcher.clear();
        List<Line> instr = fetcher.fetch();
        assertEquals(0, instr.size());
    }

    public void testAddAfterClear() throws Exception {
        Fetcher fetcher = new Fetcher();
        assertNotNull(fetcher);

        fetcher.clear();
        assertTrue(fetcher.add("decr x;"));
        List<Line> instr = fetcher.fetch();
        assertEquals(1, instr.size());
        assertTrue(instr.get(0).equals("decr x"));
    }

    public void testClearOnEmpty() throws Exception {
        Fetcher fetcher = new Fetcher();
        assertNotNull(fetcher);

        fetcher.clear();
        assertEquals(0, fetcher.fetch().size());
    }

    public void testNotTerminated() throws Exception {
        Fetcher fetcher = new Fetcher();
        assertNotNull(fetcher);

        assertTrue(fetcher.add("incr x;"));
        assertFalse(fetcher.add("decr y"));

        List<BonesException> exceptions = fetcher.getExceptions();
        if(!(exceptions.get(0) instanceof NotTerminatedException))

    }
}