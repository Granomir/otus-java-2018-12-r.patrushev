package com.patrushev.my_atm_emulator;

import java.util.Map;

public interface Dispenser {
    void giveMoney(Map cassettes, int amount);
}
