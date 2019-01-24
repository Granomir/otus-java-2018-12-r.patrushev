package com.patrushev.my_arraylist;

import java.lang.reflect.Array;
import java.util.*;

public class MyArrayList<E> implements List<E> {
    //внутренний массив - основа списка
    private E[] array;
    //текущее количество элементов в списке
    private int size = 0;
    //размер внутреннего массива
    private int capacity;

    //готов - создается массив с размером по умолчанию 10
    @SuppressWarnings("unchecked")
    public MyArrayList() {
        array = (E[]) new Object[10];
        capacity = array.length;
    }

    //готов - создается массив с переданным размером
    @SuppressWarnings("unchecked")
    public MyArrayList(int size) {
        array = (E[]) new Object[size];
        capacity = array.length;
    }

    //готов - возвращает количество элементов в списке
    @Override
    public int size() {
        return size;

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
        for (E e : array) {
            if (e == o) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    //готов - возвращает массив типа Object, содержащий все элементы списка
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    //как-то сделать проверку, что тип переданного массива соответствует или является предком дженерика, иначе при записи в него будет ошибка
    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        //здесь еще сначала проверка типа...
        if (a.length >= size) {
            for (int i = 0; i < size; i++) {
                a[i] = (T) array[i];
            }
            if (a.length > size) {
                a[size] = null;
            }
            return a;
        } else {
            T[] newArray = (T[]) Array.newInstance(a.getClass().getComponentType(), a.length);
            for (int i = 0; i < a.length; i++) {
                newArray[i] = (T) array[i];
            }
            return newArray;
        }
    }

    //готов - если после внесения элемента в массиве останется меньше 25% свободного места, то сначала происходит увеличение массива в 1.5 раза
    //если в списке уже Integer.MAX элементов, то в него больше нельзя добавлять новые элементы - выбрасывается исключение
    @Override
    public boolean add(E e) {
        if (size == Integer.MAX_VALUE) {
            throw new ArrayStoreException();
        } else {
            size++;
            if ((size) * 100 / capacity > 75) {
                enlargeCapacity((capacity * 1.5));
            }
            array[size - 1] = e;
            return true;
        }
    }

    //готов - копирует содержимое старого внутреннего массива в новый массив большего размера
    private void enlargeCapacity(double newCapacity) {
        if (newCapacity > Integer.MAX_VALUE) {
            capacity = Integer.MAX_VALUE;
        } else {
            E[] tempArray = array;
            array = Arrays.copyOf(tempArray, (int) newCapacity);
            capacity = array.length;
        }
    }

    //готов
    @Override
    public boolean remove(Object o) {
        if (contains(o)) {
            remove(indexOf(o));
            return true;
        } else {
            return false;
        }
    }

    //готов - проверяем каждый элемент переданной коллекции на совпадение с элементами списка
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    //готов - если после внесения всех элементов переданной коллекции в массиве останется меньше 25% свободного места, то сначала происходит увеличение массива
    //если в списке уже Integer.MAX элементов или при внесении элементов коллекции общей кол-во элементов превысит Integer.MAX - выбрасывается исключение
    @Override
    public boolean addAll(Collection<? extends E> c) {
        long newSize = (long) size + (long) c.size();
        if (newSize > Integer.MAX_VALUE) {
            throw new ArrayStoreException();
        } else {
            if (newSize * 100 / capacity > 75) {
                enlargeCapacity(newSize * 2);
            }
            for (E e : c) {
                array[size] = e;
                size++;
            }
            return true;
        }
    }

    //готов - если в списке уже Integer.MAX элементов или при внесении элементов коллекции общей кол-во элементов превысит Integer.MAX
    //или если переданный индекс превышает размер коллекции - выбрасывается исключение
    //если после внесения всех элементов переданной коллекции в массиве останется меньше 25% свободного места, то сначала происходит увеличение массива
    //все сущ. элементы начиная с переданного индекса переносятся вправо на значение, равное количеству элементов в переданной коллекции
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        long newSize = (long) size + (long) c.size();
        if (newSize > Integer.MAX_VALUE) {
            throw new ArrayStoreException();
        } else {
            if (newSize * 100 / capacity > 75) {
                enlargeCapacity(newSize * 2);
            }
            moveTailToRight(index, c.size());
            for (E e : c) {
                array[index] = e;
                index++;
            }
            size += c.size();
            return true;
        }
    }

    //готов - переносит хвост массива вправо
    private void moveTailToRight(int index, int offset) {
        //получаем количество итераций
        int count = size - index;
        //получаем индекс, в который надо начинать перенос
        int newIndexR = size - 1 + offset;
        //получаем индекс, с которого надо начинать перенос
        int oldIndexR = newIndexR - offset;
        for (int i = 0; i < count; i++, newIndexR--, oldIndexR--) {
            array[newIndexR] = array[oldIndexR];
            array[oldIndexR] = null;
        }
    }

    //готов - переносит хвост массива влево
    private void moveTailToLeft(int index) {
        //получаем количество итераций
        int count = size - index - 1;
        //получаем индекс, в который надо начинать перенос
        int newIndexL = index;
        //получаем индекс, с которого надо начинать перенос
        int oldIndexL = newIndexL + 1;
        for (int i = 0; i < count; i++, newIndexL++, oldIndexL++) {
            array[newIndexL] = array[oldIndexL];
            array[oldIndexL] = null;
        }
    }

    //готов
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = false;
        for (Object o : c) {
            if(remove(o)) {
                result = true;
            }
        }
        return result;
    }

    //готов
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean result = false;
        for (int i = 0; i < size; i++) {
            if (!c.contains(array[i])) {
                remove(i);
                i--;
                result = true;
            }
        }
        return result;
    }

    //готов - заменяем старый массив новым пустым того же размера и сбрасываем счетчик size
    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        array = (E[]) new Object[capacity];
        size = 0;
    }

    //готов
    @Override
    public E get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    //готов
    @Override
    public E set(int index, E element) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        E temp = array[index];
        array[index] = element;
        return temp;
    }

    //готов - если в списке уже Integer.MAX элементов или если переданный индекс превышает размер коллекции - выбрасывается исключение
    //если после внесения элемента в массиве останется меньше 25% свободного места, то сначала происходит увеличение массива
    //все сущ. элементы начиная с переданного индекса переносятся вправо на единицу
    @Override
    public void add(int index, E element) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (size == Integer.MAX_VALUE) {
            throw new ArrayStoreException();
        } else {
            size++;
            if ((size) * 100 / capacity > 75) {
                enlargeCapacity((capacity * 1.5));
            }
            moveTailToRight(index, 1);
            array[index] = element;
        }
    }

    //готов
    @Override
    public E remove(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        E temp = array[index];
        moveTailToLeft(index);
        size--;
        return temp;
    }

    //готов
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == array[i]) {
                return i;
            }
        }
        return -1;
    }

    //готов
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o == array[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }
}
