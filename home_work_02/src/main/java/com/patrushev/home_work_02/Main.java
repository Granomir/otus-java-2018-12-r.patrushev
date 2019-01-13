package com.patrushev.home_work_02;

import com.patrushev.home_work_02.factories.EmptyStringFactory;
import com.patrushev.home_work_02.factories.ObjectArrayFactory;

public class Main {
    public static void main(String[] args) {
        //создаем объект измерителя объектов
        ObjectSizeDeterminer objectSizeDeterminer = new ObjectSizeDeterminerImpl();
        //говорим измерителю определить размер объекта, который создается переданной измерителю фабрикой
        long emptyStringObjectMemorySize = objectSizeDeterminer.getMemorySizeOfObject(new EmptyStringFactory());
        System.out.println("Memory size of empty String is " + emptyStringObjectMemorySize + " bytes");
//        long objectArrayMemorySize = objectSizeDeterminer.getMemorySizeOfObject(new ObjectArrayFactory(0));
//        System.out.println("Memory size of Object array (length is 0) is " + objectArrayMemorySize + " bytes");
//        objectArrayMemorySize = objectSizeDeterminer.getMemorySizeOfObject(new ObjectArrayFactory(1));
//        System.out.println("Memory size of Object array (length is 1) is " + objectArrayMemorySize + " bytes");
//        objectArrayMemorySize = objectSizeDeterminer.getMemorySizeOfObject(new ObjectArrayFactory(2));
//        System.out.println("Memory size of Object array (length is 2) is " + objectArrayMemorySize + " bytes");
//        objectArrayMemorySize = objectSizeDeterminer.getMemorySizeOfObject(new ObjectArrayFactory(3));
//        System.out.println("Memory size of Object array (length is 3) is " + objectArrayMemorySize + " bytes");
//        objectArrayMemorySize = objectSizeDeterminer.getMemorySizeOfObject(new ObjectArrayFactory(4));
//        System.out.println("Memory size of Object array (length is 4) is " + objectArrayMemorySize + " bytes");
//        objectArrayMemorySize = objectSizeDeterminer.getMemorySizeOfObject(new ObjectArrayFactory(5));
//        System.out.println("Memory size of Object array (length is 5) is " + objectArrayMemorySize + " bytes");
//        objectArrayMemorySize = objectSizeDeterminer.getMemorySizeOfObject(new ObjectArrayFactory(6));
//        System.out.println("Memory size of Object array (length is 6) is " + objectArrayMemorySize + " bytes");
//        objectArrayMemorySize = objectSizeDeterminer.getMemorySizeOfObject(new ObjectArrayFactory(7));
//        System.out.println("Memory size of Object array (length is 7) is " + objectArrayMemorySize + " bytes");
        long objectArrayMemorySize = objectSizeDeterminer.getMemorySizeOfObject(new ObjectArrayFactory(8));
        System.out.println("Memory size of Object array (length is 8) is " + objectArrayMemorySize + " bytes");
    }
}
