package com.patrushev.my_json_object_writer;

public class Ford extends Car {
    String model = "Explorer";
    int price = 1000;

    @Override
    public String toString() {
        return "Ford{" +
                "model='" + model + '\'' +
                ", price=" + price +
                '}';
    }
}
