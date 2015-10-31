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
        Sanitizer sanitizer = new Sanitizer();
        assertNotNull(sanitizer);

        Decoder decoder = new Decoder();
        assertNotNull(decoder);

        sanitizer.add("incr x;");
        assertTrue(decoder.decode(sanitizer.fetch()));

        sanitizer.add("incr x; unknown command");
    }
}