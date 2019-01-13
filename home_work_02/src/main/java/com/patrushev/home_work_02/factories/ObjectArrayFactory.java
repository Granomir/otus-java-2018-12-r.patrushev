package com.patrushev.home_work_02.factories;

public class ObjectArrayFactory implements ObjectFactory {
    private int arrayLength;

    public ObjectArrayFactory(int arrayLength) {
        this.arrayLength = arrayLength;
    }

    @Override
    public Object createObject() {
        return new Object[arrayLength];
    }
}
