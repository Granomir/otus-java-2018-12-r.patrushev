package com.patrushev.my_atm_emulator;

import java.util.HashMap;
import java.util.Map;

public class GreedyDispenser implements Dispenser {
    /**
     * Выдает деньги по "жадному" алгоритму
     * @param cassettes - имеющиеся в банкомате купюры
     * @param requestedAmount - запрашиваемая сумма
     */
    @Override
    public void cashOut(Map<Integer, Integer> cassettes, int requestedAmount) {
        StringBuilder resultString = new StringBuilder();
        resultString.append("Выданы:\n");
        Map<Integer, Integer> banknotesIssued = new HashMap<>();
        for (int denomination : cassettes.keySet()) {
            int restOfBanknotes = cassettes.get(denomination);
            if (requestedAmount >= denomination && restOfBanknotes > 0) {
                while (restOfBanknotes > 0 && requestedAmount >= denomination) {
                    if (banknotesIssued.containsKey(denomination)) {
                        banknotesIssued.put(denomination, banknotesIssued.get(denomination) + 1);
                    } else {
                        banknotesIssued.put(denomination, 1);
                    }
                    requestedAmount -= denomination;
                    restOfBanknotes--;
                }
                if (banknotesIssued.containsKey(denomination)) {
                    resultString.append(banknotesIssued.get(denomination)).append(" банкнот номиналом ").append(denomination).append("\n");
                }
            }
        }
        if (requestedAmount > 0) {
            System.out.println("Невозможно выдать запрошенную сумму - не хватает необходимых банкнот\n");
            return;
        }
        System.out.println(resultString);
        for (Map.Entry<Integer, Integer> pair : banknotesIssued.entrySet()) {
            cassettes.put(pair.getKey(), cassettes.get(pair.getKey()) - pair.getValue());
        }
    }
}
