package com.patrushev.my_arraylist;

import java.lang.reflect.Array;
import java.util.*;

public class MyArrayList<E> implements List<E> {
    //внутренний массив - основа списка
    private E[] array;
    //текущее количество элементов в списке
    private int size = 0;
    private final int fillingDegree = 75;

    //готов - создается массив с размером по умолчанию 10
    @SuppressWarnings("unchecked")
    public MyArrayList() {
        array = (E[]) new Object[10];
    }

    //готов - создается массив с переданным размером
    @SuppressWarnings("unchecked")
    public MyArrayList(int size) {
        array = (E[]) new Object[size];
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
            if (Objects.equals(o, e)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int next = 0;

            @Override
            public boolean hasNext() {
                return next < size;
            }

            @Override
            public E next() {
                next++;
                return get(next - 1);
            }
        };
    }

    //готов - возвращает массив типа Object, содержащий все элементы списка
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    //готов
    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length >= size) {
            for (int i = 0; i < size; i++) {
                a[i] = (T) array[i];
            }
            if (a.length > size) {
                a[size] = null;
            }
            return a;
        } else {
            T[] newArray = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
            for (int i = 0; i < size; i++) {
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
            if ((size) * 100 / array.length > fillingDegree) {
                enlargeCapacity((array.length * 1.5));
            }
            array[size - 1] = e;
            return true;
        }
    }

    //готов - копирует содержимое старого внутреннего массива в новый массив большего размера
    private void enlargeCapacity(double newCapacity) {
        E[] tempArray = array;
        if (newCapacity > Integer.MAX_VALUE) {
            array = Arrays.copyOf(tempArray, Integer.MAX_VALUE);
        } else {
            array = Arrays.copyOf(tempArray, (int) newCapacity);
        }
    }

    //готов
    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index > -1) {
            remove(index);
            return true;
        } else {
            return false;
        }
    }

    //===============================

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
            if (newSize * 100 / array.length > fillingDegree) {
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
        checkIndex(index);
        long newSize = (long) size + (long) c.size();
        if (newSize > Integer.MAX_VALUE) {
            throw new ArrayStoreException();
        } else {
            if (newSize * 100 / array.length > fillingDegree) {
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
        array = (E[]) new Object[array.length];
        size = 0;
    }

    //готов
    @Override
    public E get(int index) {
        checkIndex(index);
        return array[index];
    }

    //готов
    @Override
    public E set(int index, E element) {
        checkIndex(index);
        E temp = array[index];
        array[index] = element;
        return temp;
    }

    //готов - если в списке уже Integer.MAX элементов или если переданный индекс превышает размер коллекции - выбрасывается исключение
    //если после внесения элемента в массиве останется меньше 25% свободного места, то сначала происходит увеличение массива
    //все сущ. элементы начиная с переданного индекса переносятся вправо на единицу
    @Override
    public void add(int index, E element) {
        checkIndex(index);
        if (size == Integer.MAX_VALUE) {
            throw new ArrayStoreException();
        } else {
            size++;
            if ((size) * 100 / array.length > fillingDegree) {
                enlargeCapacity((array.length * 1.5));
            }
            moveTailToRight(index, 1);
            array[index] = element;
        }
    }

    //готов
    @Override
    public E remove(int index) {
        checkIndex(index);
        E temp = array[index];
        moveTailToLeft(index);
        size--;
        return temp;
    }

    private void checkIndex(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
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

    //готов
    @Override
    public ListIterator<E> listIterator() {
        return new ListIterator<>() {
            private int next = 0;
            private int previous = -1;
            private int lastReturnedE = -1;

            @Override
            public boolean hasNext() {
                return next < size;
            }

            @Override
            public E next() {
                next++;
                previous++;
                lastReturnedE = previous;
                return get(previous);
            }

            @Override
            public boolean hasPrevious() {
                return previous > -1;
            }

            @Override
            public E previous() {
                next--;
                previous--;
                lastReturnedE = next;
                return get(next);
            }

            @Override
            public int nextIndex() {
                return next;
            }

            @Override
            public int previousIndex() {
                return previous;
            }

            //не учтены ограничения к этому методу, описанные в интерфейсе Listiterator
            @Override
            public void remove() {
                MyArrayList.this.remove(lastReturnedE);
            }

            //не учтены ограничения к этому методу, описанные в интерфейсе Listiterator
            @Override
            public void set(E e) {
                MyArrayList.this.set(lastReturnedE, e);
            }

            //не реализован
            @Override
            public void add(E e) {
                throw new UnsupportedOperationException("not implemented");
            }
        };
    }

    //не реализован
    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("not implemented");
    }

    //не реализован
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public String toString() {
        //стрёмненький
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append(array[i]).append(",");
        }
        return builder.toString();
    }
}
