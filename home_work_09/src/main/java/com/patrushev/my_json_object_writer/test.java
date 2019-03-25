package com.patrushev.my_json_object_writer;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class test {
    public static void main(String[] args) {

        MyObjectToJsonWriter test = new MyObjectToJsonWriter();

//        myObjectTest(test);
//
//        arrayListTest(test);
//
//        linkedListTest(test);
//
        mapTest(test);

//        hashSetTest(test);
    }

    private static void mapTest(MyObjectToJsonWriter test) {
        Map<String, Car> cars = new HashMap<>();
        cars.put("1", new Car());
//        cars.put("2", new Car());
        System.out.println(test.writeToJson(cars));

        System.out.println();

        Gson gson = new Gson();
        System.out.println(gson.toJson(cars));
    }

    private static void linkedListTest(MyObjectToJsonWriter test) {
        List<Car> carList = new LinkedList<>();
        carList.add(new Car());
        carList.add(new Car());
        System.out.println(test.writeToJson(carList));

        System.out.println();

        Gson gson = new Gson();
        System.out.println(gson.toJson(carList));
    }

    private static void arrayListTest(MyObjectToJsonWriter test) {
        List<Car> carList = new ArrayList<>();
        carList.add(new Car());
//        carList.add(new Car());
        System.out.println(test.writeToJson(carList));

        System.out.println();

        Gson gson = new Gson();
        System.out.println(gson.toJson(carList));
    }

    private static MyObjectToJsonWriter myObjectTest(MyObjectToJsonWriter test) {

        Ford ford = new Ford();
        System.out.println(test.writeToJson(ford));

        System.out.println();

        Gson gson = new Gson();
        System.out.println(gson.toJson(ford));
        return test;
    }

    private static void hashSetTest(MyObjectToJsonWriter test) {
        Set<Car> carList = new HashSet<>();
        carList.add(new Car());
//        carList.add(new Car());
        System.out.println(test.writeToJson(carList));

        System.out.println();

        Gson gson = new Gson();
        System.out.println(gson.toJson(carList));
    }
}
