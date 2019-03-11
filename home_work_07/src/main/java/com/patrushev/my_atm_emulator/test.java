package com.patrushev.my_atm_emulator;

import java.util.*;

public class test {
    public static void main(String[] args) {
//        Atm myAtm = new AtmImpl(new MoneyCassette(100, 1),
//                new MoneyCassette(500, 2));
//        System.out.println(myAtm.checkBalance());
//        myAtm.depositMoney(100, 1);
//        System.out.println(myAtm.checkBalance());

        Map<Integer, Integer> test = new HashMap<>();

        test.put(1, 33);
        test.put(4, 11);
        test.put(2, 44);
        test.put(3, 22);

        for (Integer integer : test.keySet()) {
            System.out.println(integer);
        }

        System.out.println();

        Set<Integer> testset;
        testset = test.keySet();
        for (Integer integer : testset) {
            System.out.println(integer);
        }
    }
}
