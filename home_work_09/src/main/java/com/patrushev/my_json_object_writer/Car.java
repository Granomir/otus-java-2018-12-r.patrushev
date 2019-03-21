package com.patrushev.my_json_object_writer;

public class Car {
    private static int wheels = 4;
    private int doors = 5;
    private int[] drivers = {1, 2, 3, 4};
    private String color = "Green";
    private boolean working = true;
    private Salon salon = new Salon();
//    private Salon salon = null;

    @Override
    public String toString() {
        return "Car{" +
                "doors=" + doors +
                ", color='" + color + '\'' +
                ", working=" + working +
                ", salon=" + salon +
                '}';
    }
}
