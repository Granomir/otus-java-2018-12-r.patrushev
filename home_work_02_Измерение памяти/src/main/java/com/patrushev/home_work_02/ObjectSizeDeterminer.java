package com.patrushev.home_work_02;

import com.patrushev.home_work_02.factories.ObjectFactory;

public class ObjectSizeDeterminer {

    public static long getMemorySizeOfObject(ObjectFactory objectFactory) {
        int size = 1_000_000;
        //создаем массив для объектов
        Object[] objects = new Object[size];
        //определем размер занятой памяти до заполнения массива объектами
        long currentlyOccupiedMemorySize1 = getCurrentlyOccupiedMemorySize();
        //заплняем массив объектами, создаваемыми фабрикой
        for (int i = 0; i < objects.length; i++) {
            //создаем объект
            objects[i] = objectFactory.createObject();
        }
        //определем размер занятой памяти после заполнения массива
        long currentlyOccupiedMemorySize2 = getCurrentlyOccupiedMemorySize();
        //размер памяти занимаемой объектом = (размер занятой памяти после заполнения массива - размер занятой памяти до заполнения массива) / кол-во элементов
        long memorySizeOfObject = (currentlyOccupiedMemorySize2 - currentlyOccupiedMemorySize1) / objects.length;
        //присваиваем массиву null для того, чтобы GC выполнил уборку
        objects = null;
        return memorySizeOfObject;
    }

    //возвращает размер занятой в данный момент памяти
    private static long getCurrentlyOccupiedMemorySize() {
        //вызов сборщика мусора
        System.gc();
        //ждем 10 мс, чтобы увеличить вероятность отработки GC???
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Runtime runtime = Runtime.getRuntime();
        //размер занятой памяти = общий размер памяти - размер свободной памяти
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
