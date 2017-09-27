package com.jb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class LastByteDistributionProcessor {
    private Path inputFilePath;

    public LastByteDistributionProcessor(Path inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public Stream<LBEntry> parse() throws IOException {
        return Files.lines(inputFilePath)
                .skip(1)
                .map(s -> s.split(","))
                .map(this::mapToLBEntry);
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

    public Stream<ProcessedLBEntry> process(Stream<LBEntry> entries) {
        Map<LBClass, Map<Long, Long>> statistics = calculateStatistics(entries);

        return statistics.entrySet().stream()
                .flatMap(lbClassMapEntry -> lbClassMapEntry.getValue().entrySet().stream()
                        .map(log2TtlbToCount ->
                                new ProcessedLBEntry(lbClassMapEntry.getKey(),
                                        log2TtlbToCount.getKey(),
                                        log2TtlbToCount.getValue())));
    }

    public Map<LBClass, Map<Long, Long>> calculateStatistics(Stream<LBEntry> entries) {
        /*Map<LBClass, Long> lbClassFreq = entries.collect(
                groupingBy(LBEntry::getLbClass, counting())
        );*/
        return entries.collect(
                groupingBy(LBEntry::getLbClass,
                        groupingBy(LBEntry::getTtlb, Collectors.counting())));
//                                Collectors.mapping(o -> getPercentage(lbClassFreq, o)
//                                        , Collectors.toSet()))));
    }

    public Long getPercentage(Map<LBClass, Long> lbClassFreq, LBEntry entry) {
        return entry.getTtlb() * 100 / lbClassFreq.get(entry.getLbClass());
    }
}
