package com.patrushev.my_atm_emulator;

public class CashImpl implements Cash {
    private final int fiveTd;
    private final int twoTd;
    private final int oneTd;
    private final int fiveHd;
    private final int twoHd;
    private final int oneHd;

    public CashImpl(int fiveTd, int twoTd, int oneTd, int fiveHd, int twoHd, int oneHd) {
        if (fiveTd < 0 || twoTd < 0 || oneTd < 0 || fiveHd < 0 || twoHd < 0 || oneHd < 0) throw new IllegalArgumentException("Нельзя положить отрицательное число банкнот");
        this.fiveTd = fiveTd;
        this.twoTd = twoTd;
        this.oneTd = oneTd;
        this.fiveHd = fiveHd;
        this.twoHd = twoHd;
        this.oneHd = oneHd;
    }

    public int getFiveTd() {
        return fiveTd;
    }

    public int getTwoTd() {
        return twoTd;
    }

    public int getOneTd() {
        return oneTd;
    }

    public int getFiveHd() {
        return fiveHd;
    }

    public int getTwoHd() {
        return twoHd;
    }

    public int getOneHd() {
        return oneHd;
    }

    public int amount() {
        return (fiveTd * 5 + twoTd * 2 + oneTd) * 1000 + (fiveHd * 5 + twoHd * 2 + oneHd) * 100;
    }
}
