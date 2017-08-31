package com.jb;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;

public class LastByteDistributionTest
        extends TestCase {
    private static final File INPUT_FILE = new File("src/test/resources/testFile.csv");

    public LastByteDistributionTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(LastByteDistributionTest.class);
    }

    public void testShouldConstructLastByteDistributionProcessor() {
        //given
        //when
        LastByteDistributionProcessor lastByteDistributionProcessor = new LastByteDistributionProcessor(INPUT_FILE);
        //then
        assertTrue(lastByteDistributionProcessor != null);
    }
}
