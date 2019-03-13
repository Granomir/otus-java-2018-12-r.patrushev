package com.patrushev;

import java.util.*;

public class AtmDepartment {
    Map<String, List<EventListener>> atms;

    public AtmDepartment() {
        atms = new HashMap<>();
        atms.put("Total Balance", new ArrayList<>());
        atms.put("Initial State", new ArrayList<>());
    }

    public int obtainTotalBalanceFromAllAtms() {
        return 0;
    }

    public void resetStateOfAllAtms() {
        
    }
}
