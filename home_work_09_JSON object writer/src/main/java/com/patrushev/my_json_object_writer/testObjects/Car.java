package com.patrushev.my_json_object_writer.testObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Car {
    private static final int wheels = 4;
    private final int doors = 5;
    private final int[] drivers = {1, 2, 3, 4};
    private final Rule[] rules = {new Rule(), new Rule(), new Rule()};
    private final Rule[][] rules1 = {{new Rule()}, {new Rule()}, {new Rule()}};
    private final String color = null;
    private final boolean working = true;
    private final Salon salon = null;
    private final ArrayList<Map<String, Rule>> rules3 = new ArrayList<>();

    {
        Map<String, Rule> maps = new HashMap<>();
        maps.put("1", null);
        rules3.add(maps);
    }

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
