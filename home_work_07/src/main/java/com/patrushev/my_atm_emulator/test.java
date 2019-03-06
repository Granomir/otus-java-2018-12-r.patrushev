package com.patrushev.my_atm_emulator;

public class test {
    public static void main(String[] args) {
        Cash cash = new RubleCash(1000, 5000, 100, 200, 500);
        System.out.println(cash.amount());
        System.out.println();
        cash.printBanknotes();
        cash.putBanknotes(1000, 1);
        cash.putBanknotes(5000, 12);
        cash.putBanknotes(200, 3);
        cash.putBanknotes(500, 2);
        cash.putBanknotes(100, 11);
        System.out.println();

        System.out.println(cash.amount());
        System.out.println();
        cash.printBanknotes();

//        cash = new RubleCash();
    }
}
