package com.patrushev.atm_department;

import com.patrushev.atm_department.atms.Atm;

import java.util.*;

@SuppressWarnings("WeakerAccess")
public class AtmDepartment {
    private ArrayList<Atm> atms;

    public AtmDepartment() {
        atms = new ArrayList<>();
    }

    public void registerAtm(Atm atm) {
        atms.add(atm);
    }

    public void obtainTotalBalanceFromAllAtms() {
        int totalBalance = 0;
        for (Atm atm : atms) {
            totalBalance += atm.checkBalance();
        }
        System.out.println(totalBalance);
    }

    public void resetStateOfAllAtms() {
        for (Atm atm : atms) {
            atm.restoreInitialState();
        }
    }
}
