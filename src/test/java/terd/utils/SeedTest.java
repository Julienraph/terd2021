package terd.utils;

import junit.framework.TestCase;

public class SeedTest extends TestCase {

    public void testGetAnswer() {
        Seed seed = new Seed();
        assertEquals(
                seed.getSeed().charAt(0),
                seed.getAnswer(Seed.SEED_LENGTH * 2)
        );
    }
}