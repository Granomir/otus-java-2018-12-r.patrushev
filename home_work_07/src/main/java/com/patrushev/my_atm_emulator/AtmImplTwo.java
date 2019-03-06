package com.patrushev.my_atm_emulator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AtmImplTwo implements Atm {
    /**
     * наличность, имеющаяся в банкомате
     */
    private Map<Integer, Integer> atmCash;

    /**
     * номиналы, с которыми работает банкомат
     */
    private int[] denominations;

    public AtmImplTwo(int... denominations) {
        if(denominations.length == 0) throw new IllegalArgumentException("Должны быть заданы используемые номиналы банкнот");
        atmCash = new HashMap<>();
        for (int denomination : denominations) {
            atmCash.put(denomination, 0);
        }
        Arrays.sort(denominations);
        int[] tempDenominations = new int[denominations.length];
        for (int i = denominations.length - 1, j = 0; i > -1; i--, j++) {
            tempDenominations[j] = denominations[i];
        }
        this.denominations = tempDenominations;
    }

    @Override
    public void depositMoney(int banknoteDenomination, int quantity) {
        int[] denominations = cash.getDenominations();
        if(!Arrays.equals(denominations, atmCash.getDenominations())) throw new IllegalArgumentException("Попытка положить деньги неподдерживаемого номинала");
        for (int denomination : denominations) {
            atmCash.putBanknotes(denomination, cash.getBanknoteCount(denomination));
        }
    }

    @Override
    public Cash withdrawMoney(int amount) {
        if (amount < 0 || amount % 100 > 0) throw new IllegalArgumentException("Запрашиваемая сумма должна быть положительным числом, кратным 100");
        if (amount > checkBalance()) throw new IllegalArgumentException("Запрашиваемая сумма превышает остаток в банкомате");

        //создаем объект разменщика и передаем ему amount и cash, созданный на основе имеющихся в ATM купюр
        //разменщик определяет набор необходимых для выдачи купюр и возвращает его в объекте cash
        //по данным объекта cash редактируется набор банкнот банкомата
        //этот объект cash возвращается методом

        //нужно учитывать количество имеющихся купюр каждого номинала
        //если в банкомате есть купюры такого номинала, с помощью которых не получится собрать ту сумму, которая запрашивается, то тоже ошибка
        //запрашиваемая сумма должна собираться только из тех купюр, которые есть в наличии

        return null;
    }

    @Override
    public int checkBalance() {
        return atmCash.amount();
    }
}
