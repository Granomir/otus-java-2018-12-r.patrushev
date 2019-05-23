package com.patrushev.atm_department;

import com.patrushev.atm_department.atms.Atm;
import com.patrushev.atm_department.atms.AtmImpl;
import com.patrushev.atm_department.atms.GreedyDispenser;
import com.patrushev.atm_department.atms.MoneyCassette;

public class Test {
    public static void main(String[] args) {
        AtmDepartment atmDepartment = new AtmDepartment();
        Atm atm1 = new AtmImpl(atmDepartment, new MoneyCassette(5000, 10),
                new MoneyCassette(2000, 10),
                new MoneyCassette(1000, 10));
        Atm atm2 = new AtmImpl(atmDepartment, new MoneyCassette(500, 10),
                new MoneyCassette(200, 10),
                new MoneyCassette(100, 10));
        Atm atm3 = new AtmImpl(atmDepartment, new MoneyCassette(50, 10),
                new MoneyCassette(10, 10),
                new MoneyCassette(5, 10));

        System.out.println();
        atmDepartment.obtainTotalBalanceFromAllAtms();
        System.out.println();

        atm1.withdrawMoney(new GreedyDispenser(), 10000);
        atm2.withdrawMoney(new GreedyDispenser(), 1000);
        atm3.withdrawMoney(new GreedyDispenser(), 100);

        atmDepartment.obtainTotalBalanceFromAllAtms();
        System.out.println();

        atmDepartment.resetStateOfAllAtms();
        atmDepartment.obtainTotalBalanceFromAllAtms();
    }
}
