package com.patrushev.my_arraylist;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MyArrayListTest {
    MyArrayList<String> testList = new MyArrayList<>();

    @Before
    public void setUp() throws Exception {
        testList = new MyArrayList<>();
    }
//
//    @After
//    public void tearDown() throws Exception {
//        testList.clear();
//    }

    @Test
    public void size() {
        Assert.assertEquals(0, testList.size());
        testList.add("1");
        testList.add("2");
        testList.add("3");
        Assert.assertEquals(3, testList.size());
    }

    @Test
    public void isEmpty() {
        Assert.assertEquals(0, testList.size());
        Assert.assertTrue(testList.isEmpty());
    }

    @Test
    public void contains() {
        testList.add("1");
        Assert.assertTrue(testList.contains("1"));
    }

    @Test
    public void iterator() {
    }

    @Test
    public void toArray() {
        testList.add("1");
        testList.add("2");
        testList.add("3");
        boolean result = true;
        Object[] testArray = testList.toArray();
        assertEquals(testArray.length, testList.size());
        for (int i = 0; i < testArray.length; i++) {
            if (!testArray[i].equals(testList.get(i))) {
                result = false;
            }
        }
        Assert.assertTrue(result);
    }

    @Test
    public void toArray1() {
        testList.add("1");
        testList.add("2");
        testList.add("3");
        boolean result = true;
        String[] testArray1 = new String[] {"5", "4", "3", "2", "1"};
        String[] testArray2 = testList.toArray(testArray1);
        for (int i = 0; i < testList.size(); i++) {
            if (!testArray2[i].equals(testList.get(i))) {
                result = false;
            }
        }
        assertNull(testArray2[testList.size()]);
        assertSame("1", testArray2[testList.size() + 1]);
        Assert.assertTrue(result);
        assertSame(testArray1, testArray2);
        String[] testArray3 = new String[2];
        String[] testArray4 = testList.toArray(testArray3);
        assertNotSame(testArray3, testArray4);
        for (int i = 0; i < testArray4.length; i++) {
            if (!testArray4[i].equals(testList.get(i))) {
                result = false;
            }
        }
        Assert.assertTrue(result);
    }

    @Test
    public void add() {
        for (int i = 0; i < 1000; i++) {
             testList.add("1");
        }
        Assert.assertEquals(1000, testList.size());
    }

    @Test
    public void remove() {
        testList.add("1");
        testList.add("2");
        testList.add("3");
        testList.add("4");
        testList.add("5");
        assertEquals(5, testList.size());
        testList.remove("1");
        Assert.assertEquals("2", testList.get(0));
        assertEquals(4, testList.size());
        testList.remove("2");
        testList.remove("3");
        assertEquals(2, testList.size());
    }

    @Test
    public void containsAll() {
        testList.add("1");
        testList.add("2");
        testList.add("3");
        testList.add("4");
        testList.add("5");
        List<String> arrayList1 = new ArrayList<>();
        arrayList1.add("1");
        arrayList1.add("2");
        arrayList1.add("3");
        Assert.assertTrue(testList.containsAll(arrayList1));
        List<String> arrayList2 = new ArrayList<>();
        arrayList2.add("1");
        arrayList2.add("6");
        Assert.assertFalse(testList.containsAll(arrayList2));
    }

    @Test
    public void addAll() {
        List<String> arrayList1 = new ArrayList<>();
        arrayList1.add("1");
        arrayList1.add("2");
        arrayList1.add("3");
        testList.addAll(arrayList1);
        assertEquals(testList.size(), arrayList1.size());
        List<String> arrayList2 = new ArrayList<>();
        arrayList2.add("4");
        arrayList2.add("5");
        arrayList2.add("6");
        testList.addAll(arrayList2);
        assertEquals(6, testList.size());
        assertSame("5", testList.get(4));
    }

    @Test
    public void addAll1() {
        testList.add("1");
        testList.add("2");
        testList.add("3");
        testList.add("4");
        testList.add("5");
        List<String> arrayList = new ArrayList<>();
        arrayList.add("6");
        arrayList.add("7");
        arrayList.add("8");
        testList.addAll(3, arrayList);
        assertEquals(8, testList.size());
        assertSame("6", testList.get(3));
        assertSame("5", testList.get(7));
    }

    @Test
    public void removeAll() {
        testList.add("1");
        testList.add("2");
        testList.add("3");
        testList.add("4");
        testList.add("5");
        List<String> arrayList1 = new ArrayList<>();
        arrayList1.add("1");
        arrayList1.add("2");
        arrayList1.add("3");
        testList.removeAll(arrayList1);
        assertEquals(2, testList.size());
        List<String> arrayList2 = new ArrayList<>();
        arrayList2.add("5");
        arrayList2.add("6");
        arrayList2.add("7");
        arrayList2.add("8");
        testList.removeAll(arrayList2);
        assertEquals(1, testList.size());
        List<String> arrayList3 = new ArrayList<>();
        arrayList3.add("11");
        assertFalse(testList.removeAll(arrayList3));
    }

    @Test
    public void retainAll() {
        testList.add("1");
        testList.add("2");
        testList.add("3");
        testList.add("4");
        testList.add("5");
        testList.add("6");
        testList.add("7");
        List<String> arrayList1 = new ArrayList<>();
        arrayList1.add("5");
        arrayList1.add("2");
        arrayList1.add("3");
        assertTrue(testList.retainAll(arrayList1));
        assertFalse(testList.retainAll(arrayList1));
        Assert.assertEquals(3, testList.size());
        Assert.assertEquals("3", testList.get(1));
    }

    @Test
    public void clear() {
        testList.add("1");
        testList.add("2");
        testList.add("3");
        testList.add("4");
        testList.add("5");
        testList.add("6");
        testList.add("7");
        testList.clear();
        assertTrue(testList.isEmpty());
    }

    @Test
    public void get() {
        testList.add("1");
        testList.add("2");
        testList.add("3");
        assertEquals("2", testList.get(1));
    }

    @Test
    public void set() {
        testList.add("1");
        testList.add("2");
        testList.add("3");
        testList.set(1, "33");
        assertEquals("33", testList.get(1));
    }

    @Test
    public void add1() {
        testList.add("1");
        testList.add("2");
        testList.add("3");
        testList.add(1, "11");
        testList.add(1, "22");
        testList.add(1, "33");
        assertEquals("33", testList.get(1));
        assertEquals("22", testList.get(2));
        assertEquals("11", testList.get(3));
        assertEquals("2", testList.get(4));
        assertEquals(6, testList.size());
    }

    @Test
    public void remove1() {
        testList.add("1");
        testList.add("2");
        testList.add("3");
        testList.remove(1);
        assertEquals("3", testList.get(1));
        assertEquals(2, testList.size());
    }

    @Test
    public void indexOf() {
        testList.add("1");
        testList.add("2");
        testList.add("3");
        testList.add("2");
        assertEquals(1, testList.indexOf("2"));
    }

    @Test
    public void lastIndexOf() {
        testList.add("1");
        testList.add("2");
        testList.add("3");
        testList.add("2");
        assertEquals(3, testList.lastIndexOf("2"));
    }

    @Test
    public void listIterator() {
    }

    @Test
    public void listIterator1() {
    }

    @Test
    public void subList() {
    }
}