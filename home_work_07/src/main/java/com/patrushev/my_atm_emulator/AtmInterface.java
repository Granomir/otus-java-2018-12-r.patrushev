package com.patrushev.my_atm_emulator;

public interface AtmInterface {
    void depositMoney(CashInterface cash);

    int withdrawMoney();

    int checkBalance();
}
