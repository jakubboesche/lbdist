package com.jb;

import java.util.stream.Stream;

public enum LBClass {
    LESSTHAN100kB(100 * 1024, "<100K"),
    BETWEEN100kBAND1MB(1024 * 1024, ">100K<1M"),
    BETWEEN1MAND10MB(10 * 1024 * 1024, ">1M<10M"),
    BETWEEN10MBAND100MB(100 * 1024 * 1024, ">10M<100M"),
    BETWEEN100MBAND1GB(1024 * 1024 * 1024, ">100M<1G"),
    ANY(Integer.MAX_VALUE, "MAX");

    private long upperLimit;
    private String description;

    LBClass(int upperLimit, String description) {
        this.upperLimit = upperLimit;
        this.description = description;
    }

    public static LBClass valueOf(long size) {
        return Stream.of(values()).filter(lbClass -> size <= lbClass.upperLimit)
                .findFirst().orElse(ANY);
    }

    public String getDescription() {
        return description;
    }
}
