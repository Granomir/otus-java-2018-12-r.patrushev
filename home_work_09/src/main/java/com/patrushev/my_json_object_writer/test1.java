package com.patrushev.my_json_object_writer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class test1 {
    public static void main(String[] args) {
        MyObjectToJsonWriter myObjectToJsonWriter = new MyObjectToJsonWriter();
        String[] a = new String[10];
        for (int i = 0; i < a.length; i++) {
            a[i] = String.valueOf(i);

        }
        System.out.println(myObjectToJsonWriter.writeToJson(a));
//        System.out.println(jsonObject.toJSONString());

//        JSONArray jsonArray = new JSONArray();
//        for (int i = 0; i < a.length; i++) {
//            jsonArray.add(a[i]);
//        }
//        System.out.println(jsonArray.toJSONString());

    }
}
