package com.patrushev.my_atm_emulator;

public class Cash implements CashInterface {
    private final int fiveTd;
    private final int twoTd;
    private final int oneTd;
    private final int fiveHd;
    private final int twoHd;
    private final int oneHd;

    public Cash(int fiveTd, int twoTd, int oneTd, int fiveHd, int twoHd, int oneHd) {
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
