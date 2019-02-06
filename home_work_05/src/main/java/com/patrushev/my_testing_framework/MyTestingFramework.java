package com.patrushev.my_testing_framework;

import com.patrushev.my_testing_framework.annotations.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class MyTestingFramework {

    public static <T> void runTests(Class<T> testingClass) {

        try {
            //получаем конструктор переданного класса
            Constructor<T> constructor = testingClass.getDeclaredConstructor();

            //получаем все методы класса
            Method[] allMethods = testingClass.getDeclaredMethods();

            //распределяем их в соответствии с аннотациями
            List<Method> beforeAllMethods = new ArrayList<>();
            List<Method> beforeEachMethods = new ArrayList<>();
            List<Method> testMethods = new ArrayList<>();
            List<Method> afterEachMethods = new ArrayList<>();
            List<Method> afterAllMethods = new ArrayList<>();

            for (Method method : allMethods) {
                if (method.getAnnotation(BeforeAll.class) != null) beforeAllMethods.add(method);
                if (method.getAnnotation(BeforeEach.class) != null) beforeEachMethods.add(method);
                if (method.getAnnotation(Test.class) != null) testMethods.add(method);
                if (method.getAnnotation(AfterEach.class) != null) afterEachMethods.add(method);
                if (method.getAnnotation(AfterAll.class) != null) afterAllMethods.add(method);
            }

            //вызываем методы BeforeAll
            for (Method beforeAllMethod : beforeAllMethods) {
                beforeAllMethod.invoke(null);
            }

            //запускаем цикл тестов
            for (Method testMethod : testMethods) {
                //создаем новый объект тестового класса
                T testClassInstance = constructor.newInstance();
                //вызываем методы BeforeEach
                for (Method beforeEachMethod : beforeEachMethods) {
                    beforeEachMethod.invoke(testClassInstance);
                }
                //вызываем метод Test
                testMethod.invoke(testClassInstance);
                //вызываем методы AfterEach
                for (Method afterEachMethod : afterEachMethods) {
                    afterEachMethod.invoke(testClassInstance);
                }
            }


            //запускаем методы AfterAll
            for (Method afterAllMethod : afterAllMethods) {
                afterAllMethod.invoke(null);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
