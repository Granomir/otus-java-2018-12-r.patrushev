package com.patrushev.my_json_object_writer;

import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class test1 {
    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
//        List<Car> carList = new ArrayList<>();
//        carList.add(new Ford());
//        jsonObject.put("carList", carList);
//        System.out.println(jsonObject.toJSONString());
//
//        System.out.println();
//
//        Gson gson = new Gson();
//        System.out.println(gson.toJson(carList));


        Map<String, Car> cars = new HashMap<>();
//        cars.put("1", new Car());
//        jsonObject.put("map", cars);
//        System.out.println(jsonObject.toJSONString());
//
//        System.out.println();
//
//        Gson gson = new Gson();
//        System.out.println(gson.toJson(cars));

//        System.out.println(cars instanceof Map);

//        System.out.println(gson.toJson(1));

//        Set<Car> carSet = new LinkedHashSet<>();
//        System.out.println(carSet instanceof Collection);


//        List<Class> interfaces12 = new ArrayList<>();
//        List<Class> interfaces = getAllInterfaces(carSet.getClass(), interfaces12);
//
//        System.out.println(interfaces.contains(Collection.class));
//
//        for (Class anInterface : interfaces) {
//            System.out.println(anInterface);
//        }




//        System.out.println(Arrays.asList(carSet.getClass().getAllInterfaces()).contains(HashSet.class));

    }

    private static List<Class> getAllInterfaces(Class iclass, List<Class> interfaces12) {
        Class<?>[] interfaces1 = iclass.getInterfaces();
        for (int i = 0; i < interfaces1.length; i++) {
            Class<?> aClass = interfaces1[i];
            interfaces12.add(aClass);
            if (aClass.getInterfaces().length != 0) {
                getAllInterfaces(aClass, interfaces12);
            }
        }
        return interfaces12;
    }
}
