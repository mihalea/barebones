package ro.mihalea.cadets.barebones.logic;

import junit.framework.TestCase;

import java.util.List;

/**
 * Created by Mircea on 31-Oct-15.
 */
public class FetcherTest extends TestCase {

    public void testAddSingleStatement() throws Exception {
        Fetcher fetcher = new Fetcher();
        assertNotNull(fetcher);

        fetcher.add("incr x;");
        List<String> instr = fetcher.fetch();
        assertEquals(1, instr.size());
        assertEquals("incr x", instr.get(0));

        instr = fetcher.fetch();
        assertEquals(0, instr.size());
    }

    public void testSameLineStatements() throws Exception {
        Fetcher fetcher = new Fetcher();
        assertNotNull(fetcher);

        fetcher.add("incr x; decr x;");
        List<String> instr = fetcher.fetch();
        assertEquals(2, instr.size());
        assertEquals("incr x", instr.get(0));
        assertEquals("decr x", instr.get(1));

        instr = fetcher.fetch();
        assertEquals(0, instr.size());
    }

    public void testMultipleLines() throws Exception {
        Fetcher fetcher = new Fetcher();
        assertNotNull(fetcher);

        fetcher.add("incr x;");
        fetcher.add("decr x; clear y;");
        List<String> instr = fetcher.fetch();
        assertEquals(3, instr.size());
        assertEquals("incr x", instr.get(0));
        assertEquals("decr x", instr.get(1));
        assertEquals("clear y", instr.get(2));

        instr = fetcher.fetch();
        assertEquals(0, instr.size());
    }

    public void testClearAfterAdd() throws Exception {
        Fetcher fetcher = new Fetcher();
        assertNotNull(fetcher);

        fetcher.add("incr x; decr x;");
        fetcher.clear();
        List<String> instr = fetcher.fetch();
        assertEquals(0, instr.size());
    }

    public void testAddAfterClear() throws Exception {
        Fetcher fetcher = new Fetcher();
        assertNotNull(fetcher);

        fetcher.clear();
        fetcher.add("decr x;");
        List<String> instr = fetcher.fetch();
        assertEquals(1, instr.size());
        assertEquals("decr x", instr.get(0));
    }

    public void testClearOnEmpty() throws Exception {
        Fetcher fetcher = new Fetcher();
        assertNotNull(fetcher);

        fetcher.clear();
        assertEquals(0, fetcher.fetch().size());
    }
}