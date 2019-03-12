package com.patrushev.my_atm_emulator;

import java.util.Map;

public interface Dispenser {
    void cashOut(Map<Integer, Integer> cassettes, int amount);
}
