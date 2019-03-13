package com.patrushev.atm_department.atms;

import com.patrushev.atm_department.AtmDepartment;

import java.util.*;

@SuppressWarnings("WeakerAccess")
public class AtmImpl implements Atm, EventListener {
    /**
     * наличность, имеющаяся в банкомате (номинал и количество)
     */
    private TreeMap<Integer, Integer> cassettes;

    /**
     * объект снимка, в который конструктором записывается начальное состояние объекта ATM
     */
    private Memento memento;

    /**
     * в конструктор передаются денежные кассеты (любое количество) с заданными номиналом банкнот, с которыми будет работать банкомат, а также их начальное количество.
     * Кроме этого конструктор сохраняет начальное состояние ATM в объекте Memento и регистрирует ATM в ATM Department.
     * @param newCassettes - денежные кассеты
     */
    public AtmImpl(AtmDepartment atmDepartment, MoneyCassette... newCassettes) {
        if(newCassettes.length == 0) throw new IllegalArgumentException("Должны быть вставлены денежные кассеты");
        cassettes = new TreeMap<>((o1, o2) -> o2 - o1);
        for (MoneyCassette cassette : newCassettes) {
            cassettes.put(cassette.getDenomination(), cassette.getQuantity());
        }
        memento = new Memento();
        atmDepartment.registerAtm(this);
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

    /**
     * восстанавливает исходное состояние банкомата
     */
    public void restoreInitialState() {
        cassettes = memento.getInitialState();
    }

    /**
     * Класс отвечающий за получение и хранение исходного состояния банкомата
     */
    public class Memento {
        private final TreeMap<Integer, Integer> initialCassettes;

        public Memento() {
            this.initialCassettes = new TreeMap<>(cassettes);
        }

        public TreeMap<Integer, Integer> getInitialState() {
            return initialCassettes;
        }
    }
}
