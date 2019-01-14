package com.patrushev.home_work_02;

import com.patrushev.home_work_02.factories.EmptyStringFactory;
import com.patrushev.home_work_02.factories.IntArrayFactory;
import com.patrushev.home_work_02.factories.ObjectFactoryByClonning;

public class Main {
    public static void main(String[] args) {
        //специализированная фабрика, которая сама создает объекты - работает быстро
        long emptyStringObjectMemorySize = ObjectSizeDeterminer.getMemorySizeOfObject(new EmptyStringFactory());
        System.out.println("Memory size of empty String is " + emptyStringObjectMemorySize + " bytes");

        //универсальная фабрика, которая клонирует переданный ей объект - работает намного медленней
        emptyStringObjectMemorySize = ObjectSizeDeterminer.getMemorySizeOfObject(new ObjectFactoryByClonning(new String()));
        System.out.println("Memory size of empty String is " + emptyStringObjectMemorySize + " bytes");

        //специализированная фабрика, которая сама создает объекты - работает быстро
        long intArrayMemorySize = ObjectSizeDeterminer.getMemorySizeOfObject(new IntArrayFactory(0));
        System.out.println("Memory size of int array (length is 0) is " + intArrayMemorySize + " bytes");
        intArrayMemorySize = ObjectSizeDeterminer.getMemorySizeOfObject(new IntArrayFactory(4));
        System.out.println("Memory size of int array (length is 4) is " + intArrayMemorySize + " bytes");
        intArrayMemorySize = ObjectSizeDeterminer.getMemorySizeOfObject(new IntArrayFactory(8));
        System.out.println("Memory size of int array (length is 8) is " + intArrayMemorySize + " bytes");

        //универсальная фабрика, которая клонирует переданный ей объект - работает намного медленней
        intArrayMemorySize = ObjectSizeDeterminer.getMemorySizeOfObject(new ObjectFactoryByClonning(new int[0]));
        System.out.println("Memory size of int array (length is 0) is " + intArrayMemorySize + " bytes");
        intArrayMemorySize = ObjectSizeDeterminer.getMemorySizeOfObject(new ObjectFactoryByClonning(new int[4]));
        System.out.println("Memory size of int array (length is 4) is " + intArrayMemorySize + " bytes");
        intArrayMemorySize = ObjectSizeDeterminer.getMemorySizeOfObject(new ObjectFactoryByClonning(new int[8]));
        System.out.println("Memory size of int array (length is 8) is " + intArrayMemorySize + " bytes");
    }
}
