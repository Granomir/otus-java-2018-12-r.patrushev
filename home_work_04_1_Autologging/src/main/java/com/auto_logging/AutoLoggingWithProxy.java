package com.auto_logging;

public class AutoLoggingWithProxy {

    public static void main(String[] args) {
        MyInterface myInterfaceObject1 = (MyInterface) MyProxifier.getProxifiedObject(new MyInterfaceImpl1());
        myInterfaceObject1.methodA("1.1");
        System.out.println("-------------------------------");
        myInterfaceObject1.methodB("1.2");
        System.out.println("-------------------------------");
        MyInterface myInterfaceObject2 = (MyInterface) MyProxifier.getProxifiedObject(new MyInterfaceImpl2());
        myInterfaceObject2.methodA("2.1");
        System.out.println("-------------------------------");
        myInterfaceObject2.methodB("2.2");
        System.out.println("-------------------------------");
    }
}
