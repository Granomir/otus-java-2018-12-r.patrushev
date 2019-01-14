package com.patrushev.home_work_02;

import com.patrushev.home_work_02.factories.EmptyStringFactory;
import com.patrushev.home_work_02.factories.IntArrayFactory;
import com.patrushev.home_work_02.factories.ObjectFactoryByClonning;

public class Main {
    public static void main(String[] args) {
        //специализированная фабрика, которая сама создает объекты - работает быстро
        long emptyStringObjectMemorySize = ObjectSizeDeterminer.getMemorySizeOfObject(new EmptyStringFactory());
        System.out.println("Special fabric - Memory size of empty String is " + emptyStringObjectMemorySize + " bytes");
        long intArrayMemorySize = ObjectSizeDeterminer.getMemorySizeOfObject(new IntArrayFactory(0));
        System.out.println("Special fabric - Memory size of int array (length is 0) is " + intArrayMemorySize + " bytes");
        intArrayMemorySize = ObjectSizeDeterminer.getMemorySizeOfObject(new IntArrayFactory(4));
        System.out.println("Special fabric - Memory size of int array (length is 4) is " + intArrayMemorySize + " bytes");
        intArrayMemorySize = ObjectSizeDeterminer.getMemorySizeOfObject(new IntArrayFactory(8));
        System.out.println("Special fabric - Memory size of int array (length is 8) is " + intArrayMemorySize + " bytes\n");

        //универсальная фабрика, которая клонирует переданный ей объект - работает медленней
        emptyStringObjectMemorySize = ObjectSizeDeterminer.getMemorySizeOfObject(new ObjectFactoryByClonning(new String()));
        System.out.println("Universal fabric - Memory size of empty String is " + emptyStringObjectMemorySize + " bytes");
        intArrayMemorySize = ObjectSizeDeterminer.getMemorySizeOfObject(new ObjectFactoryByClonning(new int[0]));
        System.out.println("Universal fabric - Memory size of int array (length is 0) is " + intArrayMemorySize + " bytes");
        intArrayMemorySize = ObjectSizeDeterminer.getMemorySizeOfObject(new ObjectFactoryByClonning(new int[4]));
        System.out.println("Universal fabric - Memory size of int array (length is 4) is " + intArrayMemorySize + " bytes");
        intArrayMemorySize = ObjectSizeDeterminer.getMemorySizeOfObject(new ObjectFactoryByClonning(new int[8]));
        System.out.println("Universal fabric - Memory size of int array (length is 8) is " + intArrayMemorySize + " bytes\n");

        //инструментирование
        emptyStringObjectMemorySize = InstrumentationAgent.getObjectSize(new String());
        System.out.println("Instrumentation - Memory size of empty String is " + emptyStringObjectMemorySize + " bytes");
        intArrayMemorySize = InstrumentationAgent.getObjectSize(new int[0]);
        System.out.println("Instrumentation - Memory size of int array (length is 0) is " + intArrayMemorySize + " bytes");
        intArrayMemorySize = InstrumentationAgent.getObjectSize(new int[4]);
        System.out.println("Instrumentation - Memory size of int array (length is 4) is " + intArrayMemorySize + " bytes");
        intArrayMemorySize = InstrumentationAgent.getObjectSize(new int[8]);
        System.out.println("Instrumentation - Memory size of int array (length is 8) is " + intArrayMemorySize + " bytes");


    }
}
