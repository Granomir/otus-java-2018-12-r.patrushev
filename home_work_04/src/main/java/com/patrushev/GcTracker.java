package com.patrushev;

import java.util.ArrayList;
import java.util.List;


public class GcTracker {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        try {
            List strings = new ArrayList();
            Runtime runtime = Runtime.getRuntime();
            int count = 10000000;
            while (true) {
//                System.out.println("до добавления: " + runtime.freeMemory());
                for (int i = 0; i < count; i++) {
                    strings.add(new Object());
                }
//                System.out.println(strings.size());
//                System.out.println("после добавления: " + runtime.freeMemory());

                for (int i = 0; i < count / 2; i++) {
                    strings.remove(strings.size() - 1);
                }
//                System.out.println(strings.size());
            }
        } finally {
            long stopTime = System.nanoTime();
            System.out.println((stopTime - startTime) / 1000 / 1000 / 1000 + " секунд");
        }
    }
}
