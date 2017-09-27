package com.jb;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
        List<LBEntry> outputDistribution = processor.parse();
        //then
        //200,600000,2,2048,123.456,5
        LBEntry lbEntry = outputDistribution.get(0);
        assertEquals(2 + 2048 + 5, lbEntry.getTtlb());
        assertEquals(BETWEEN100kBAND1MB, lbEntry.getLbClass());
    }

    public void testShouldRunParser() throws IOException {
        //given
        LastByteDistributionProcessor processor = new LastByteDistributionProcessor(path);
        //when
        List<LBEntry> entries = processor.parse();
        //then
        assertTrue(entries != null);
        assertEquals(10, entries.size());
    }

    public void testShouldCalculateStatistics() throws IOException {
        //given
        LastByteDistributionProcessor processor = new LastByteDistributionProcessor(path);
        //when
        Map<LBClass, Map<Long, Double>> statistics = processor.calculateStatistics(processor.parse());
        //then
        assertTrue(statistics != null);
        assertEquals(2, statistics.keySet().size());
        double sum = statistics.values().stream()
                .map(Map::values).flatMap(Collection::stream)
                .mapToDouble(percentage -> percentage).sum();
        assertEquals(100.0, sum);
    }

    public void testShouldCreateXYChart() throws IOException {
        //given
        LastByteDistributionProcessor processor = new LastByteDistributionProcessor(path);
        //when
        Map<LBClass, Map<Long, Double>> statistics = processor.calculateStatistics(processor.parse());
        File chart = LBChartCreator.createXYChart(statistics, "testFile.png");
        //then
        assertTrue(chart != null);
    }
}
