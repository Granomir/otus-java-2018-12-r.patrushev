package com.patrushev;

@SuppressWarnings("WeakerAccess")
public class GCjobReport {
    private final int minuteNumber;
    private final long youngGCcount;
    private final long youngGCDurationCount;
    private final long oldGCcount;
    private final long oldGCDurationCount;

    public GCjobReport(int minuteNumber, long youngGCcount, long youngGCDurationCount, long oldGCcount, long oldGCDurationCount) {
        this.minuteNumber = minuteNumber;
        this.youngGCcount = youngGCcount;
        this.youngGCDurationCount = youngGCDurationCount;
        this.oldGCcount = oldGCcount;
        this.oldGCDurationCount = oldGCDurationCount;
    }

    public int getMinuteNumber() {
        return minuteNumber;
    }

    public long getYoungGCcount() {
        return youngGCcount;
    }

    public long getYoungGCDurationCount() {
        return youngGCDurationCount;
    }

    public long getOldGCcount() {
        return oldGCcount;
    }

    public long getOldGCDurationCount() {
        return oldGCDurationCount;
    }
}
