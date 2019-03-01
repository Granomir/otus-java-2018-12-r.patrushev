package com.patrushev.my_atm_emulator;

public class AtmImpl implements Atm {
    private int fiveTd;
    private int twoTd;
    private int oneTd;
    private int fiveHd;
    private int twoHd;
    private int oneHd;

    public AtmImpl(int fiveTd, int twoTd, int oneTd, int fiveHd, int twoHd, int oneHd) {
        this.fiveTd = fiveTd;
        this.twoTd = twoTd;
        this.oneTd = oneTd;
        this.fiveHd = fiveHd;
        this.twoHd = twoHd;
        this.oneHd = oneHd;
    }

    public AtmImpl() {
        fiveTd = 0;
        twoTd = 0;
        oneTd = 0;
        fiveHd = 0;
        twoHd = 0;
        oneHd = 0;
    }

    @Override
    public void depositMoney(Cash cash) {
        fiveTd += cash.getFiveTd();
        twoTd += cash.getTwoTd();
        oneTd += cash.getOneTd();
        fiveHd += cash.getFiveHd();
        twoHd += cash.getTwoHd();
        oneHd += cash.getOneHd();
    }

    @Override
    public Cash withdrawMoney(int amount) {
        if (amount < 0 || amount % 100 > 0) throw new IllegalArgumentException("Запрашиваемая сумма должна быть положительным числом, кратным 100");
        if (amount > checkBalance()) throw new IllegalArgumentException("Запрашиваемая сумма превышает остаток в банкомате");
        //нужно учитывать количество имеющихся купюр каждого номинала
        //если в банкомате есть купюры такого номинала, с помощью которых не получится собрать ту сумму, которая запрашивается, то тоже ошибка
        //запрашиваемая сумма должна собираться только из тех купюр, которые есть в наличии

        return null;
    }

    @Override
    public int checkBalance() {
        return (fiveTd * 5 + twoTd * 2 + oneTd) * 1000 + (fiveHd * 5 + twoHd * 2 + oneHd) * 100;
    }
}
