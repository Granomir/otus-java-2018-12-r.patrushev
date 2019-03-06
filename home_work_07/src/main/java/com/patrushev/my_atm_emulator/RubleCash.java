package com.patrushev.my_atm_emulator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RubleCash implements Cash {
    private Map<Integer, Integer> cash;
    private int[] denominations;

    public RubleCash(int... denominations) {
        if(denominations.length == 0) throw new IllegalArgumentException("Должны быть заданы номиналы банкнот");
        cash = new HashMap<>();
        for (int denomination : denominations) {
            cash.put(denomination, 0);
        }
        Arrays.sort(denominations);
        int[] tempDenominations = new int[denominations.length];
        for (int i = denominations.length - 1, j = 0; i > -1; i--, j++) {
            tempDenominations[j] = denominations[i];
        }
        this.denominations = tempDenominations;
    }

    @Override
    public Map getBanknotes() {
        return cash;
    }

    @Override
    public void printBanknotes() {
        for (int denomination : denominations) {
            System.out.println(denomination + " рублей - " + cash.get(denomination) + " штук");
        }
    }

    @Override
    public int amount() {
        int amount = 0;
        for (Integer integer : cash.keySet()) {
            amount += integer * cash.get(integer);
        }
        return amount;
    }

    @Override
    public void putBanknotes(int denomination, int count) {
        cash.put(denomination, count);
    }

    @Override
    public int[] getDenominations() {
        return denominations;
    }

    @Override
    public int getBanknoteCount(int denomination) {
        return cash.get(denomination);
    }
}
