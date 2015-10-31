package ro.mihalea.cadets.barebones.logic;

import junit.framework.TestCase;

import java.util.List;

/**
 * Created by Mircea on 31-Oct-15.
 */
public class FetcherTest extends TestCase {

    public void testAdd() throws Exception {
        Fetcher fetcher = new Fetcher();
        assertNotNull(fetcher);

        fetcher.add("incr x");
        List<String> instr = fetcher.fetch();
        assertEquals(1, instr.size());
        assertEquals("incr x", instr.get(0));

        instr = fetcher.fetch();
        assertEquals(0, instr.size());

    }
}