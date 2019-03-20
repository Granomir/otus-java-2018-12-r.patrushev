package com.patrushev.my_json_object_writer;

import org.json.simple.JSONObject;

import java.lang.reflect.Field;

public class MyObjectToJsonWriter implements ObjectToJsonWriter {
    /**
     * 1.26 - javax.json
     * 1.39 - simple json
     * 0.57 - reflection
     */

    @Override
    public <T> String writeToJson(T object) {
        //создаю Json-объект
        JSONObject jsonObj = new JSONObject();
        //определяю класс объекта
        Class objectClass = object.getClass();
        //получаю все поля объекта
        Field[] fields = objectClass.getFields();
        //кладу в Json-объект поля исходного объекта со значениями
        for (Field field : fields) {
            try {
                jsonObj.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //вытаскиваю поля объекта и их значения (включая поля родителей)
        //записываю все поля-значения в simple json
        //записываю результат в файл
        return null;
    }

    @Override
    public <T> T readFromJson(String json, Class<T> objectClass) {
        //создаю новый объект переданного класса и задаю в нем значения полей из json
        //задание полей через рефлекшн (т.к. поля могут быть private и без сеттеров)
        return null;
    }
}
