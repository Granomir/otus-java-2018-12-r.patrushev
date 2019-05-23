package com.patrushev;

@SuppressWarnings("WeakerAccess")
public class GCjobReport {

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

    private int minuteNumber;
    private long youngGCcount;
    private long youngGCDurationCount;
    private long oldGCcount;
    private long oldGCDurationCount;
}
