package com.patrushev.multithreaded_sorting;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        int[] arr = {1, 2, 5, 4, 32, 12, 99, 76, 11, 54};
        long begin = System.nanoTime();
        ForkJoinPool pool = new ForkJoinPool();
        int[] sortedArr = pool.invoke(new Sorter(arr));
        long end = System.nanoTime();
        System.out.println("Сортировка заняла " + (end - begin) + " наносекунд");
        System.out.println(Arrays.toString(sortedArr));
    }
}
