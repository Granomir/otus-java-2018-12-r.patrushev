package com.patrushev.home_work_02.factories;

public class IntArrayFactory implements ObjectFactory {
    private int arrayLength;

    public IntArrayFactory(int arrayLength) {
        this.arrayLength = arrayLength;
    }

    @Override
    public Object createObject() {
        return new int[arrayLength];
    }
}
