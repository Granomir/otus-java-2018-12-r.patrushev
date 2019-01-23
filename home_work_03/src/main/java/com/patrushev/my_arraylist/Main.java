package com.patrushev.my_arraylist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        List<String> myList = new MyArrayList<>();
//        Collections.addAll(myList, "1", "2", "3");
//        Object[] ints = new Object[10];
//        Object[] integers = myList.toArray(ints);
//        for (Object integer : integers) {
//            System.out.println(integer);
//        }
        List<String> list = new ArrayList<>();
        for (String s : list) {
            System.out.println(s);
        }
    }
}
