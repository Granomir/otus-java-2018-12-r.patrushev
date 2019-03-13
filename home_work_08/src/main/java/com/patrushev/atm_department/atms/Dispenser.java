package com.patrushev.atm_department.atms;

import java.util.Map;

public interface Dispenser {
    void cashOut(Map<Integer, Integer> cassettes, int amount);
}
