package com.jb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class LastByteDistributionFileProcessor extends LastByteDistributionProcessor {
    private Path inputFilePath;

    public LastByteDistributionFileProcessor(Path inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public List<LBEntry> parse() throws IOException {
        Stream<String> lines = Files.lines(inputFilePath);
        return parse(lines);
    }
}
