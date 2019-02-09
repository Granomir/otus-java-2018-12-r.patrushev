package com.patrushev.my_testing_framework;

import com.patrushev.my_testing_framework.annotations.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class MyTestingFramework {
    private static List<Method> beforeAllMethods = new ArrayList<>();
    private static List<Method> beforeEachMethods = new ArrayList<>();
    private static List<Method> testMethods = new ArrayList<>();
    private static List<Method> afterEachMethods = new ArrayList<>();
    private static List<Method> afterAllMethods = new ArrayList<>();

    private static int successfulTestsCount;

    public static <T> void runTests(Class<T> testingClass) {
        try {
            //получаем конструктор переданного класса
            Constructor<T> constructor = testingClass.getDeclaredConstructor();
            prepareTests(testingClass);
            makePreliminaryWork();
            makeTests(constructor);
            makeFinalWork();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        System.out.println("Успешно выполнены " + successfulTestsCount + " тестов из " + testMethods.size());
    }

    private static void makeFinalWork() throws IllegalAccessException, InvocationTargetException {
        //запускаем методы AfterAll
        for (Method afterAllMethod : afterAllMethods) {
            afterAllMethod.invoke(null);
        }
    }

    private static <T> void makeTests(Constructor<T> constructor) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        //запускаем цикл тестов
        for (Method testMethod : testMethods) {
            //создаем новый объект тестового класса
            T testClassInstance = constructor.newInstance();
            //вызываем методы BeforeEach
            for (Method beforeEachMethod : beforeEachMethods) {
                beforeEachMethod.invoke(testClassInstance);
            }
            try {
                //вызываем метод Test
                testMethod.invoke(testClassInstance);
                System.out.println("Тест завершен успешно!");
                successfulTestsCount++;
            } catch (InvocationTargetException | IllegalAccessException e) {
                if (e.getCause() instanceof AssertionError) {
                    System.out.println("Тест завершен неудачно!");
                } else {
                    System.out.println("Не получилось закончить тест!");
                    e.printStackTrace();
                }
            }
            //вызываем методы AfterEach
            for (Method afterEachMethod : afterEachMethods) {
                afterEachMethod.invoke(testClassInstance);
            }
        }
    }

    private static void makePreliminaryWork() throws IllegalAccessException, InvocationTargetException {
        //вызываем методы BeforeAll
        for (Method beforeAllMethod : beforeAllMethods) {
            beforeAllMethod.invoke(null);
        }
    }

    private static <T> void prepareTests(Class<T> testingClass) {
        //получаем все методы класса
        Method[] allMethods = testingClass.getDeclaredMethods();

        //распределяем их в соответствии с аннотациями
        for (Method method : allMethods) {
            if (method.getAnnotation(BeforeAll.class) != null) beforeAllMethods.add(method);
            if (method.getAnnotation(BeforeEach.class) != null) beforeEachMethods.add(method);
            if (method.getAnnotation(Test.class) != null) testMethods.add(method);
            if (method.getAnnotation(AfterEach.class) != null) afterEachMethods.add(method);
            if (method.getAnnotation(AfterAll.class) != null) afterAllMethods.add(method);
        }
    }
}
