package com.jb;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import static com.jb.LBClass.*;

public class LBClassTest
        extends TestCase {

    public LBClassTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(LBClassTest.class);
    }

    public void testShouldRecognizeClass() {
        assertEquals(LESSTHAN100kB, LBClass.valueOf(1));
        assertEquals(BETWEEN100kBAND1MB, LBClass.valueOf(132000));
        assertEquals(BETWEEN1MAND10MB, LBClass.valueOf(5 * 1024 * 1024));
        assertEquals(BETWEEN10MBAND100MB, LBClass.valueOf(50 * 1024 * 1024));
        assertEquals(BETWEEN100MBAND1GB, LBClass.valueOf(500 * 1024 * 1024));
    }
}
