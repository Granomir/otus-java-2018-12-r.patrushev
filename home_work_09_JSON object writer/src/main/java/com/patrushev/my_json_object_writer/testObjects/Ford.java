package com.patrushev.my_json_object_writer.testObjects;

public class Ford extends Car {
    private String model = "Explorer";
    private int price = 1000;

    @Override
    public String toString() {
        return "Ford{" +
                "model='" + model + '\'' +
                ", price=" + price +
                '}';
    }
}
