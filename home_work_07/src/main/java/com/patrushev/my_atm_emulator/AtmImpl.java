package com.patrushev.my_atm_emulator;

import java.util.*;

public class AtmImpl implements Atm {
    /**
     * наличность, имеющаяся в банкомате (номинал и количество)
     */
    private TreeMap<Integer, Integer> atmCash;

    /**
     * отсортированный по убыванию перечень номиналов, с которыми работает банкомат
     */
    private Set<Integer> denominations;

    /**
     * в конструктор передаются денежные кассеты (любое количество) с заданными номиналом банкнот, с которыми будет работать банкомат, а также их начальное количество.
     * @param cassettes - денежные кассеты
     */
    public AtmImpl(MoneyCassette... cassettes) {
        if(cassettes.length == 0) throw new IllegalArgumentException("Должны быть вставлены денежные кассеты");
        atmCash = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        for (MoneyCassette cassette : cassettes) {
            atmCash.put(cassette.getDenomination(), cassette.getQuantity());
        }
        this.denominations = atmCash.keySet();
        for (Integer denomination : denominations) {
            System.out.println(denomination);
        }
    }

    /**
     * Внесение банкнот в банкомат - в случае, если указанный номинал не поддерживается банкоматом - выбрасывается исключение
     * @param banknoteDenomination - номинал банкноты
     * @param quantity - количество банкнот
     */
    @Override
    public void depositMoney(int banknoteDenomination, int quantity) {
        if (denominations.contains(banknoteDenomination)) {
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
