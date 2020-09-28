package com.patrushev.home_work_02.factories;

public class EmptyStringFactory implements ObjectFactory {
    @Override
    public Object createObject() {
        return "";
    }
}
