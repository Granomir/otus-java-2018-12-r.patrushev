package com.patrushev.my_atm_emulator;

import java.util.*;

public class AtmImpl1 implements Atm {
    /**
     * наличность, имеющаяся в банкомате (номинал и количество)
     */
    private Map<Integer, Integer> atmCash;

    /**
     * отсортированный по убыванию перечень номиналов, с которыми работает банкомат
     */
    private int[] denominations;

    /**
     * в конструктор передаются денежные кассеты (любое количество) с заданными номиналом банкнот, с которыми будет работать банкомат, а также их начальное количество.
     * @param cassettes - денежные кассеты
     */
    public AtmImpl1(MoneyCassette... cassettes) {
        if(cassettes.length == 0) throw new IllegalArgumentException("Должны быть вставлены денежные кассеты");
        atmCash = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        for (MoneyCassette cassette : cassettes) {
            atmCash.put(cassette.getDenomination(), cassette.getQuantity());
        }
        int[] unsortedDenominations = new int[cassettes.length];
        int k = 0;
        for (Integer integer : atmCash.keySet()) {
            unsortedDenominations[k] = integer;
            k++;
        }
//        Arrays.sort(unsortedDenominations);
//        int[] sortedDenominations = new int[unsortedDenominations.length];
//        for (int i = unsortedDenominations.length - 1, j = 0; i > -1; i--, j++) {
//            sortedDenominations[j] = unsortedDenominations[i];
//        }
//        this.denominations = sortedDenominations;
        this.denominations = unsortedDenominations;
    }

    /**
     * Внесение банкнот в банкомат - в случае, если указанный номинал не поддерживается банкоматом - выбрасывается исключение
     * @param banknoteDenomination - номинал банкноты
     * @param quantity - количество банкнот
     */
    @Override
    public void depositMoney(int banknoteDenomination, int quantity) {
        if (atmCash.keySet().contains(banknoteDenomination)) {
            atmCash.put(banknoteDenomination, atmCash.get(banknoteDenomination) + quantity);
        } else {
            throw new IllegalArgumentException("Данный номинал не поддерживается банкоматом.");
        }
    }

    //передавать алгоритм выдачи как интерфейс
    @Override
    public void withdrawMoney(int amount) {
        if (amount < 0 || amount % 100 > 0) throw new IllegalArgumentException("Запрашиваемая сумма должна быть положительным числом, кратным 100");
        if (amount > checkBalance()) throw new IllegalArgumentException("Запрашиваемая сумма превышает остаток в банкомате");

        //создаем объект разменщика и передаем ему amount и cash, созданный на основе имеющихся в ATM купюр
        //разменщик определяет набор необходимых для выдачи купюр и возвращает его в объекте cash
        //по данным объекта cash редактируется набор банкнот банкомата
        //этот объект cash возвращается методом

        //нужно учитывать количество имеющихся купюр каждого номинала
        //если в банкомате есть купюры такого номинала, с помощью которых не получится собрать ту сумму, которая запрашивается, то тоже ошибка
        //запрашиваемая сумма должна собираться только из тех купюр, которые есть в наличии
    }

    /**
     * @return - возвращает текущую сумму, имеющуюся в банкомате
     */
    @Override
    public int checkBalance() {
        int balance = 0;
        for (Integer integer : atmCash.keySet()) {
            balance += integer * atmCash.get(integer);
        }
        return balance;
    }
}
