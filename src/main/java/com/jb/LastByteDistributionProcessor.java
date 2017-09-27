package com.jb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class LastByteDistributionProcessor {
    private Path inputFilePath;

    public LastByteDistributionProcessor(Path inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public List<LBEntry> parse() throws IOException {
        return Files.lines(inputFilePath)
                .skip(1)
                .map(s -> s.split(","))
                .map(this::mapToLBEntry)
                .collect(toList());
    }

    private LBEntry mapToLBEntry(String[] strings) {
//        http_status,obj_sz,req_time,transfer_time,ts,turn_time
        long objSize = Long.valueOf(strings[1]);
        long reqTime = Long.valueOf(strings[2]);
        long transferTime = Long.valueOf(strings[3]);
        long turnaroundTime = Long.valueOf(strings[5]);

        long ttlb = reqTime + transferTime + turnaroundTime;

        return new LBEntry(ttlb, LBClass.valueOf(objSize));
    }

    Map<LBClass, Map<Long, Double>> calculateStatistics(List<LBEntry> entries) {
        return entries.stream().collect(
                groupingBy(LBEntry::getLbClass,
                        groupingBy(LBEntry::getTtlb,
                                collectingAndThen(
                                        Collectors.counting(),
                                        count -> count * 100.0 / entries.size()
                                ))));
    }
}
