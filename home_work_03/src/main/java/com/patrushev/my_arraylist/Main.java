package com.patrushev.my_arraylist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> myList1 = new MyArrayList<>();
//        myList.add("1");
//        myList.add("2");
//        myList.add("3");
//        for (String s : myList1) {
//            System.out.println(s);
//        }



        Collections.addAll(myList1, "3", "2", "1");
//        for (String s : myList1) {
//            System.out.println(s);
//        }
//
//        List<String> myList2 = new MyArrayList<>();
//        Collections.addAll(myList2, "4", "5", "6", "7");
//
//        Collections.copy(myList2, myList1);
//        for (String s : myList2) {
//            System.out.println(s);
//        }

        //===============

        Collections.sort(myList1);
        for (String s : myList1) {
            System.out.println(s);
        }

        Collections.sort(myList1, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return -1 * o1.compareTo(o2);
            }
        });
        for (String s : myList1) {
            System.out.println(s);
        }
//        System.out.println(0 > 0);
    }
}
