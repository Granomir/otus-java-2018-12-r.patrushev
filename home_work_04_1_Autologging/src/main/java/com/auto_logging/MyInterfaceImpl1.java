package com.auto_logging;

public class MyInterfaceImpl1 implements MyInterface {

    @Override
    public void methodA(String s) {
        System.out.println("Я метод А объекта Impl1. Мне передали аргумент " + s);
    }

    @Override
    public void methodB(String s) {
        System.out.println("Я метод B объекта Impl1. Мне передали аргумент " + s);
    }
}
