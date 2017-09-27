package com.jb;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

import static com.jb.LBClass.BETWEEN100kBAND1MB;

public class LastByteDistributionTest
        extends TestCase {
    private static final String INPUT_FILE_NAME = "src/test/resources/testFile.csv";
    private Path path;

    public LastByteDistributionTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(LastByteDistributionTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        path = FileSystems.getDefault().getPath(INPUT_FILE_NAME);
    }

    public void testShouldConstructLastByteDistributionProcessor() {
        //given
        //when
        LastByteDistributionProcessor processor = new LastByteDistributionProcessor(path);
        //then
        assertTrue(processor != null);
    }

    public void testShouldParseRow() throws IOException {
        //given
        LastByteDistributionProcessor processor = new LastByteDistributionProcessor(path);
        //when
        Stream<LBEntry> outputDistribution = processor.parse();
        //then
        //200,600000,2,2048,123.456,5
        LBEntry lbEntry = outputDistribution.findFirst().get();
        assertEquals(2 + 2048 + 5, lbEntry.getTtlb());
        assertEquals(BETWEEN100kBAND1MB, lbEntry.getLbClass());
    }

    public void testShouldRunParser() throws IOException {
        //given
        LastByteDistributionProcessor processor = new LastByteDistributionProcessor(path);
        //when
        Stream<LBEntry> entries = processor.parse();
        //then
        assertTrue(entries != null);
        assertEquals(10, entries.count());
    }

    public void testShouldRunProcessor() throws IOException {
        //given
        LastByteDistributionProcessor processor = new LastByteDistributionProcessor(path);
        //when
        Stream<ProcessedLBEntry> outputDistribution = processor.process(processor.parse());
        //then
        assertTrue(outputDistribution != null);
        assertTrue(outputDistribution.count() > 0);
    }

    public void testShouldCreateXYChart() throws IOException {
        //given
        LastByteDistributionProcessor processor = new LastByteDistributionProcessor(path);
        //when
        Map<LBClass, Map<Long, Long>> statistics = processor.calculateStatistics(processor.parse());
        File chart = LBChartCreator.createXYChart(statistics, "testFile.png");
        //then
        assertTrue(chart != null);
    }
}
