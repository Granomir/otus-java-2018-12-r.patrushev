package com.patrushev.my_arraylist;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        List<Integer> myRandomList = new MyArrayList<>();
        for (int i = 0; i < 30; i++) {
            myRandomList.add(ThreadLocalRandom.current().nextInt(0, 100 + 1));
        }
        System.out.println("random integers:");
        System.out.println(myRandomList);
        Integer[] randomIntegers = myRandomList.toArray(new Integer[]{});
        System.out.println(randomIntegers.length);

        List<Integer> myList1 = new MyArrayList<>();
        Collections.addAll(myList1, randomIntegers);
        System.out.println("myList1 after addAll:");
        System.out.println(myList1);
        System.out.println();

        List<Integer> myList2 = new MyArrayList<>();
        for (int i = 0; i < 40; i++) {
            myList2.add(ThreadLocalRandom.current().nextInt(0, 100 + 1));
        }
        System.out.println("myList2:");
        System.out.println(myList2);
        System.out.println();

        Collections.copy(myList2, myList1);
        System.out.println("myList2 after copy:");
        System.out.println(myList2);
        System.out.println();

        Collections.sort(myList1);
        System.out.println("myList1 after sort:");
        System.out.println(myList1);
        System.out.println();

        Collections.sort(myList2, (o1, o2) -> -1 * o1.compareTo(o2));
        System.out.println("myList2 after sort desc:");
        System.out.println(myList2);
    }
}
