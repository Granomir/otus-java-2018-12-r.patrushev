package com.patrushev.home_work_02.factories;

public class IntArrayFactory implements ObjectFactory {
    private int arrayLength;

    public IntArrayFactory(int arrayLength) {
        this.arrayLength = arrayLength;
    }

    @Override
    public Object createObject() {
        int[] intArray = new int[arrayLength];
        if (arrayLength > 0) {
            for (int i = 0; i < intArray.length; i++) {
                intArray[i] = i;
            }
        }
        return intArray;
    }
}
