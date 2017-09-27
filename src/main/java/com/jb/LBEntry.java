package com.jb;

public class LBEntry {
    private long ttlb;
    private LBClass lbClass;

    public LBEntry(long ttlb, LBClass lbClass) {
        this.ttlb = ttlb;
        this.lbClass = lbClass;
    }

    public long getTtlb() {
        return ttlb;
    }

    public LBClass getLbClass() {
        return lbClass;
    }

    public int getTtlbLog2() {
        return 64 - Long.numberOfLeadingZeros(ttlb);
    }
}
