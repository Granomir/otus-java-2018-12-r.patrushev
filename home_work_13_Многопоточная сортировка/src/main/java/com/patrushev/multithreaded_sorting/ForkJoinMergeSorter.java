package com.patrushev.multithreaded_sorting;

import java.util.concurrent.RecursiveTask;

public class ForkJoinMergeSorter extends RecursiveTask<int[]> {

    private final int[] arrayA;

    public ForkJoinMergeSorter(int[] arrayA) {
        this.arrayA = arrayA;
    }

    @Override
    protected int[] compute() {
        if (arrayA == null) {
            return null;
        }
        if (arrayA.length < 2) {
            return arrayA;
        }
        int[] arrayB = new int[arrayA.length / 2];
        System.arraycopy(arrayA, 0, arrayB, 0, arrayA.length / 2);

        int[] arrayC = new int[arrayA.length - arrayA.length / 2];
        System.arraycopy(arrayA, arrayA.length / 2, arrayC, 0, arrayA.length - arrayA.length / 2);

        final ForkJoinMergeSorter leftForkJoinMergeSorter = new ForkJoinMergeSorter(arrayB);
        leftForkJoinMergeSorter.fork();
        final ForkJoinMergeSorter rightForkJoinMergeSorter = new ForkJoinMergeSorter(arrayC);
        rightForkJoinMergeSorter.fork();

        arrayB = leftForkJoinMergeSorter.join();
        arrayC = rightForkJoinMergeSorter.join();

        return mergeArray(arrayB, arrayC);
    }

    private static int[] mergeArray(int[] arrayA, int[] arrayB) {

        int[] arrayC = new int[arrayA.length + arrayB.length];
        int positionA = 0, positionB = 0;

        for (int i = 0; i < arrayC.length; i++) {
            if (positionA == arrayA.length) {               //уже прошли весь массив А
                arrayC[i] = arrayB[positionB];          //начинаем просто копировать остаток массива В
                positionB++;
            } else if (positionB == arrayB.length) {        //уже прошли весь массив B
                arrayC[i] = arrayA[positionA];          //начинаем просто копировать остаток массива A
                positionA++;
            } else if (arrayA[positionA] < arrayB[positionB]) {
                arrayC[i] = arrayA[positionA];
                positionA++;
            } else {
                arrayC[i] = arrayB[positionB];
                positionB++;
            }
        }
        return arrayC;
    }
}
