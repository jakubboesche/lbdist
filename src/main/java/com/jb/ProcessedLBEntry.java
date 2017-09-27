package com.jb;

public class ProcessedLBEntry {
    LBClass lbClass;
    long log2ttlb;
    long count;

    public ProcessedLBEntry(LBClass lbClass, long log2ttlb, long count) {
        this.lbClass = lbClass;
        this.log2ttlb = log2ttlb;
        this.count = count;
    }
}