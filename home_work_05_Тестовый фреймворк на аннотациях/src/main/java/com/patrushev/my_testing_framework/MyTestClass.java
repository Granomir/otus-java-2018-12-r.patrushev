package com.patrushev.my_testing_framework;

import com.patrushev.my_testing_framework.annotations.*;

public class MyTestClass {

    @BeforeAll
    public static void beforeAllMethod1() {
        System.out.println("beforAll #1");
    }

    @BeforeAll
    public static void beforeAllMethod2() {
        System.out.println("beforAll #2");
    }

    @BeforeEach
    public void beforeEachMethod1() {
        System.out.println("beforEach #1");
    }

    @BeforeEach
    public void beforeEachMethod2() {
        System.out.println("beforEach #2");
    }

    @AfterEach
    public void afterEachMethod1() {
        System.out.println("afterEach #1");
    }

    @AfterEach
    public void afterEachMethod2() {
        System.out.println("afterEach #2");
    }

    @Test
    public void test1() {
        System.out.println("test1");
        throw new AssertionError();
    }

    @Test
    public void test2() {
        System.out.println("test2");
    }

    @Test
    public void test3() {
        System.out.println("test3");
        throw new AssertionError();
    }

    @Test
    public void test4() {
        System.out.println("test4");
        throw new NullPointerException();
    }

    @AfterAll
    public static void afterAllMethod1() {
        System.out.println("afterAll #1");
    }

    @AfterAll
    public static void afterAllMethod2() {
        System.out.println("afterAll #2");
    }
}
