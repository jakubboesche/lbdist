package com.jb;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.jb.LBClass.BETWEEN100kBAND1MB;
import static com.jb.LBClass.LESSTHAN100kB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LastByteDistributionTest {
    private static final String INPUT_FILE_NAME = "src/test/resources/testFile.csv";
    private static final String INPUT_FILE_NAME_INVALID = "src/test/resources/testFile_missing_column.csv";
    private Path path = FileSystems.getDefault().getPath(INPUT_FILE_NAME);

    @Test
    public void shouldConstructLastByteDistributionProcessor() {
        LastByteDistributionProcessor processor = new LastByteDistributionFileProcessor(path);

        assertThat(processor).isNotNull();
    }

    @Test
    public void shouldParseRow() throws IOException {
        LastByteDistributionFileProcessor processor = new LastByteDistributionFileProcessor(path);

        List<LBEntry> outputDistribution = processor.parse();

        //200,600000,2,2048,123.456,5
        LBEntry lbEntry = outputDistribution.get(0);
        assertThat(lbEntry.getTtlb()).isEqualTo(2 + 2048 + 5);
        assertThat(lbEntry.getLbClass()).isEqualTo(BETWEEN100kBAND1MB);
    }

    @Test
    public void shouldThrowExceptionParsingInvalidRow() {
        Path invalidFilePath = FileSystems.getDefault().getPath(INPUT_FILE_NAME_INVALID);
        LastByteDistributionFileProcessor processor = new LastByteDistributionFileProcessor(invalidFilePath);

        assertThatThrownBy(() -> {
            List<LBEntry> ignored = processor.parse();
        }).isInstanceOf(InvalidFileFormatException.class);
    }

    @Test
    public void shouldRunParser() throws IOException {
        LastByteDistributionFileProcessor processor = new LastByteDistributionFileProcessor(path);

        List<LBEntry> entries = processor.parse();

        assertThat(entries).hasSize(10);
    }

    @Test
    public void shouldCalculateStatistics() throws IOException {
        LastByteDistributionFileProcessor processor = new LastByteDistributionFileProcessor(path);

        Map<LBClass, Map<Long, Double>> statistics = processor.calculateStatistics(processor.parse());

        assertThat(statistics).isNotNull();
        assertThat(statistics).hasSize(2);
        double sum = statistics.values().stream()
                .map(Map::values).flatMap(Collection::stream)
                .mapToDouble(percentage -> percentage).sum();
        assertThat(sum).isEqualTo(100.0);
        assertThat(statistics.get(LESSTHAN100kB).get(1L)).isEqualTo(10.0);
        assertThat(statistics.get(LESSTHAN100kB).get(6L)).isEqualTo(20.0);
        assertThat(statistics.get(LESSTHAN100kB).get(41L)).isEqualTo(10.0);
        assertThat(statistics.get(LESSTHAN100kB).get(61L)).isEqualTo(10.0);
        assertThat(statistics.get(BETWEEN100kBAND1MB).get(769L)).isEqualTo(10.0);
        assertThat(statistics.get(BETWEEN100kBAND1MB).get(2055L)).isEqualTo(10.0);
        assertThat(statistics.get(BETWEEN100kBAND1MB).get(7931L)).isEqualTo(20.0);
        assertThat(statistics.get(BETWEEN100kBAND1MB).get(30229L)).isEqualTo(10.0);
    }

    @Test
    public void shouldCreateXYChartFile() throws IOException {
        LastByteDistributionFileProcessor processor = new LastByteDistributionFileProcessor(path);

        Map<LBClass, Map<Long, Double>> statistics = processor.calculateStatistics(processor.parse());
        File chart = LBChartCreator.createChartFile(statistics, "testFile.png");

        assertThat(chart).isNotNull();
    }

    @Test
    public void shouldCreateXYChartByteStream() throws IOException {
        LastByteDistributionFileProcessor processor = new LastByteDistributionFileProcessor(path);

        Map<LBClass, Map<Long, Double>> statistics = processor.calculateStatistics(processor.parse());
        ByteArrayOutputStream chartStream = LBChartCreator.createChartStream(statistics);

        assertThat(chartStream).isNotNull();
        assertThat(chartStream.size()).isGreaterThan(0);
    }
}
