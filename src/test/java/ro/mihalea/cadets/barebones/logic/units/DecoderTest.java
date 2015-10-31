package ro.mihalea.cadets.barebones.logic.units;

import junit.framework.TestCase;


/**
 * Created by Mircea on 31-Oct-15.
 */
public class DecoderTest extends TestCase {
    public void testCreation() throws Exception {
        Decoder decoder = new Decoder();
        assertNotNull(decoder);
    }

    public void testUnknownInstruction() throws Exception {
        Fetcher fetcher = new Fetcher();
        assertNotNull(fetcher);

        Decoder decoder = new Decoder();
        assertNotNull(decoder);

        fetcher.add("incr x;");
        assertTrue(decoder.decode(fetcher.fetch()));

        fetcher.add("incr x; unknown command");
    }
}