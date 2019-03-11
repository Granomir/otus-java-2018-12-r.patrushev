package com.patrushev.my_atm_emulator;

public interface Atm {
    void depositMoney(int banknoteDenomination, int quantity);

    void withdrawMoney(Dispenser dispenser, int amount);

    int checkBalance();
}
