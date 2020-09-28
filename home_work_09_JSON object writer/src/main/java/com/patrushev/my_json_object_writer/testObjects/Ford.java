package com.patrushev.my_json_object_writer.testObjects;

public class Ford extends Car {
    private final String model = "Explorer";
    private final int price = 1000;

    @Override
    public String toString() {
        return "Ford{" +
                "model='" + model + '\'' +
                ", price=" + price +
                '}';
    }
}
