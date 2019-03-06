package com.patrushev.my_atm_emulator;

import java.util.Map;

public interface Cash {
    Map getBanknotes();

    void printBanknotes();

    int amount();

    void putBanknotes(int denomination, int count);

    int[] getDenominations();

    int getBanknoteCount(int denomination);
}
