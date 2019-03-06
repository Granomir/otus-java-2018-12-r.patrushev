package com.patrushev.my_atm_emulator;

public interface MoneyChanger {
    Cash getBanknotes(Cash atmCash, int amount);
}
