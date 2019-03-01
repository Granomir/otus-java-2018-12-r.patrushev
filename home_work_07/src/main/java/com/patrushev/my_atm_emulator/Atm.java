package com.patrushev.my_atm_emulator;

public interface Atm {
    void depositMoney(Cash cash);

    Cash withdrawMoney(int amount);

    int checkBalance();
}
