package com.patrushev.my_json_object_writer;

public class Car {
    private static int wheels = 4;
    private int doors = 5;
    private String color = "Green";
    private boolean working = true;

    @Override
    public String toString() {
        return "Car{" +
                "wheels=" + wheels +
                ", doors=" + doors +
                ", color='" + color + '\'' +
                ", working=" + working +
                '}';
    }
}
