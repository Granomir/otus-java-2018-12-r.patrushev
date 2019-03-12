package com.patrushev.my_atm_emulator;

import java.util.*;

@SuppressWarnings("WeakerAccess")
public class AtmImpl implements Atm {
    /**
     * наличность, имеющаяся в банкомате (номинал и количество)
     */
    private TreeMap<Integer, Integer> cassettes;

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
    }

    /**
     * Внесение банкнот в банкомат - в случае, если указанный номинал не поддерживается банкоматом - выбрасывается исключение
     * @param banknoteDenomination - номинал банкноты
     * @param quantity - количество банкнот
     */
    @Override
    public void depositMoney(int banknoteDenomination, int quantity) {
        if (cassettes.keySet().contains(banknoteDenomination)) {
            cassettes.put(banknoteDenomination, cassettes.get(banknoteDenomination) + quantity);
        } else {
            throw new IllegalArgumentException("Данный номинал не поддерживается банкоматом.");
        }
    }

    /**
     * Выдает запрошенную сумму набором банкнот в соответствии с выбранным алгоритмом
     * @param dispenser - предоставляет алгоритм выдачи банкнот
     * @param amount - запрашиваемая сумма
     */
    @Override
    public void withdrawMoney(Dispenser dispenser, int amount) {
        if (amount < 0 || amount % 100 > 0) throw new IllegalArgumentException("Запрашиваемая сумма должна быть положительным числом, кратным 100");
        if (amount > checkBalance()) throw new IllegalArgumentException("Запрашиваемая сумма превышает остаток в банкомате");
        dispenser.cashOut(cassettes, amount);
    }

    /**
     * @return - возвращает текущую сумму, имеющуюся в банкомате
     */
    @Override
    public int checkBalance() {
        int balance = 0;
        for (Integer denomination : cassettes.keySet()) {
            balance += denomination * cassettes.get(denomination);
        }
        return balance;
    }
}
