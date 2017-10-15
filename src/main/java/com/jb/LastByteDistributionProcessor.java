package com.jb;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class LastByteDistributionProcessor {
    public List<LBEntry> parse(Stream<String> lines) {
        return lines.skip(1)
                .map(s -> s.split(","))
                .map(this::mapToLBEntry)
                .collect(toList());
    }

    public Map<LBClass, Map<Long, Double>> calculateStatistics(List<LBEntry> entries) {
        return entries.stream().collect(
                groupingBy(LBEntry::getLbClass,
                        groupingBy(LBEntry::getTtlb,
                                collectingAndThen(
                                        counting(),
                                        count -> count * 100.0 / entries.size()
                                ))));
    }

    private LBEntry mapToLBEntry(String[] strings) {
        if (strings.length < 6) {
            throw new InvalidFileFormatException("missing column");
        }
//        http_status,obj_sz,req_time,transfer_time,ts,turn_time
        long objSize = Long.parseLong(strings[1]);
        long reqTime = Long.parseLong(strings[2]);
        long transferTime = Long.parseLong(strings[3]);
        long turnaroundTime = Long.parseLong(strings[5]);

        long ttlb = reqTime + transferTime + turnaroundTime;

        return new LBEntry(ttlb, LBClass.valueOf(objSize));
    }
}
