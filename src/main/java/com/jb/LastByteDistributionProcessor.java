package com.jb;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
                                        Collectors.counting(),
                                        count -> count * 100.0 / entries.size()
                                ))));
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
}
