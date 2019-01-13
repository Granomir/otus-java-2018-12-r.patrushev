package com.patrushev.home_work_02.factories;

public class SimpleObjectFactory implements ObjectFactory {

    @Override
    public Object createObject() {
        return new Object();
    }
}
