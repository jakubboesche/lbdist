package com.jb;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;

public class LastByteDistribution {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("No input file");
        }

        Path path = FileSystems.getDefault().getPath(args[0]);
        LastByteDistributionFileProcessor processor = new LastByteDistributionFileProcessor(path);
        try {
            Map<LBClass, Map<Long, Double>> statistics = processor.calculateStatistics(processor.parse());
            File chart = LBChartCreator.createChartFile(statistics, path.getFileName().toString().replace(".csv", ".png"));

            System.out.println("created file " + chart.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
