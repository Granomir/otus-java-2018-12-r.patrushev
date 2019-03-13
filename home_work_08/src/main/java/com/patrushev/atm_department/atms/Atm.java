package com.patrushev.atm_department.atms;

public interface Atm {
    void depositMoney(int banknoteDenomination, int quantity);

    void withdrawMoney(Dispenser dispenser, int amount);

    int checkBalance();
}
