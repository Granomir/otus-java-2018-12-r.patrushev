package com.patrushev.my_arraylist;

import java.util.*;

public class MyArrayList<T> implements List<T> {
    //внутренний массив - основа списка
    T[] array;
    //текущее количество элементов в списке
    private long size = 0;
    //размер внутреннего массива
    private long capacity;

    //готов - создается массив с размером по умолчанию 10
    @SuppressWarnings("unchecked")
    public MyArrayList() {
        array = (T[]) new Object[10];
        capacity = array.length;
    }

    //готов - создается массив с переданным размером
    @SuppressWarnings("unchecked")
    public MyArrayList(int size) {
        array = (T[]) new Object[size];
        capacity = array.length;
    }

    //готов - возвращает количество элементов в списке
    @Override
    public int size() {
        if (size > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else {
            return (int) size;
        }
    }

    //готов - возвращает true, если размер списка равен 0
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    //готов
    @Override
    public boolean contains(Object o) {
        if (size == 0) return false;
        for (T t : array) {
            if (t == o) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    //готов - возвращает массив типа Object, содержащий все элементы списка
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, (int) size);
    }

    //как-то сделать проверку, что тип переданного массива соответствует или является предком дженерика, иначе при записи в него будет ошибка
    @SuppressWarnings("unchecked")
    @Override
    public <E> E[] toArray(E[] a) {
        if (a == null) {
            throw new NullPointerException();
        }
        //проверка типа...
        if (a.length >= size) {
            for (int i = 0; i < size; i++) {
                a[i] = (E) array[i];
            }
            if (a.length > size) {
                a[(int) size] = null;
            }
            return a;
        }
        return (E[]) Arrays.copyOf(array, (int) size);
    }

    //готов - если после внесения элемента в массиве останется мнеьше 25% свободного места, то сначала происходит увеличение массива в 1.5 раза
    //после добавления происходит проверка, что в той ячейке массива, в которую добавлялся новый элемент, действительно там находится
    @Override
    public boolean add(T t) {
        if ((size + 1) * 100 / capacity > 75) {
            enlargeCapacity();
        }
        array[(int) size] = t;


        //проверка что объект действительно добавился после выполнения этого метода (true или false) - нужно учесть что может быть два одинаковых объекта после этого
//        if (array[(int) size - 1] == null)
        return array[(int) size] == t;
    }

    //готов - копирует содержимое старого внутреннего массива в новый массив размером в 1.5 больше старого
    private void enlargeCapacity() {
        T[] tempArray = array;
        array = Arrays.copyOf(tempArray, (int) (capacity * 1.5));
        capacity = array.length;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public T get(int index) {
        return null;
    }

    @Override
    public T set(int index, T element) {
        return null;
    }

    @Override
    public void add(int index, T element) {

    }

    @Override
    public T remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }
}
