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
    public static void beforeEachMethod1() {
        System.out.println("beforEach #1");
    }

    @BeforeEach
    public static void beforeEachMethod2() {
        System.out.println("beforEach #2");
    }

    @AfterEach
    public static void afterEachMethod1() {
        System.out.println("afterEach #1");
    }

    @AfterEach
    public static void afterEachMethod2() {
        System.out.println("afterEach #2");
    }

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    public void test2() {
        System.out.println("test2");
    }

    @Test
    public void test3() {
        System.out.println("test3");
    }

    @Test
    public void test4() {
        System.out.println("test4");
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
