package com.patrushev.my_atm_emulator;

import java.util.*;

public class AtmImpl implements Atm {
    /**
     * наличность, имеющаяся в банкомате (номинал и количество)
     */
    private TreeMap<Integer, Integer> cassettes;

    /**
     * отсортированный по убыванию перечень номиналов, с которыми работает банкомат
     */
    private Set<Integer> denominations;

    /**
     * в конструктор передаются денежные кассеты (любое количество) с заданными номиналом банкнот, с которыми будет работать банкомат, а также их начальное количество.
     * @param newCassettes - денежные кассеты
     */
    public AtmImpl(MoneyCassette... newCassettes) {
        if(newCassettes.length == 0) throw new IllegalArgumentException("Должны быть вставлены денежные кассеты");
        cassettes = new TreeMap<>((o1, o2) -> o2 - o1);
        for (MoneyCassette cassette : newCassettes) {
            cassettes.put(cassette.getDenomination(), cassette.getQuantity());
        }
        this.denominations = cassettes.keySet();
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
            cassettes.put(banknoteDenomination, cassettes.get(banknoteDenomination) + quantity);
        } else {
            throw new IllegalArgumentException("Данный номинал не поддерживается банкоматом.");
        }
    }

    //передавать алгоритм выдачи как интерфейс
    @Override
    public void withdrawMoney(Dispenser dispenser, int amount) {
        if (amount < 0 || amount % 100 > 0) throw new IllegalArgumentException("Запрашиваемая сумма должна быть положительным числом, кратным 100");
        if (amount > checkBalance()) throw new IllegalArgumentException("Запрашиваемая сумма превышает остаток в банкомате");
        dispenser.giveMoney(cassettes, amount);
    }

    /**
     * @return - возвращает текущую сумму, имеющуюся в банкомате
     */
    @Override
    public int checkBalance() {
        int balance = 0;
        for (Integer integer : cassettes.keySet()) {
            balance += integer * cassettes.get(integer);
        }
        return balance;
    }
}
