package com.jb;

public class ProcessedLBEntry {
    LBClass lbClass;
    long log2ttlb;
    double percentage;

    public ProcessedLBEntry(LBClass lbClass, long log2ttlb, double percentage) {
        this.lbClass = lbClass;
        this.log2ttlb = log2ttlb;
        this.percentage = percentage;
    }
}