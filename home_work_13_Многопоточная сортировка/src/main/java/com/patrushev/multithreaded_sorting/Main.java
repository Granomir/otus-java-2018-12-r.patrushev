package com.patrushev.multithreaded_sorting;

import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        //сортировка в 4 потока начинает работать быстрее однопоточной только если элементов не менее 10 млн
        int elementsCount = 10_000_000;
        int[] arr = new int[elementsCount];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * elementsCount);
        }
        singleSorting(arr);
        fourMultiSorting(arr);
        fullMultiSorting(arr);
    }

    private static void singleSorting(int[] arr) {
        long begin = System.currentTimeMillis();
        int[] ints = SingleThreadMergeSorter.sortArray(arr);
        long end = System.currentTimeMillis();
        System.out.println("однопоточная сортировка заняла " + (end - begin) + " милисекунд");
//        System.out.println(Arrays.toString(ints));
    }

    private static void fourMultiSorting(int[] arr) {
        long begin = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool(4);
        int[] ints = pool.invoke(new ForkJoinMergeSorter(arr));
        long end = System.currentTimeMillis();
        System.out.println("Многопоточная (в 4 потока) сортировка заняла " + (end - begin) + " милисекунд");
//        System.out.println(Arrays.toString(ints));
    }

    private static void fullMultiSorting(int[] arr) {
        long begin = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool();
        int[] ints = pool.invoke(new ForkJoinMergeSorter(arr));
        long end = System.currentTimeMillis();
        System.out.println("Многопоточная (без ограничения потоков) сортировка заняла " + (end - begin) + " милисекунд");
//        System.out.println(Arrays.toString(ints));
    }
}
