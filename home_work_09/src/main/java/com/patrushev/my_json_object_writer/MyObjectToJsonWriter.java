package com.patrushev.my_json_object_writer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MyObjectToJsonWriter implements ObjectToJsonWriter {
    /**
     * 1.26 - javax.json
     * 1.39 - simple json
     * 0.57 - reflection
     */


    /**
     * в текущей реализации вложенные объекты не сериализуются!!!!!!!!!!!!!!!!
     *
     * @param object - сериализуемый объект
     * @param <T>    - тип сериализуемого объекта
     * @return - сериализованный объект (строка) в формате JSON
     */
    @Override
    public <T> String writeToJson(T object) {
        JSONObject jsonObj = getJsonObject(object);
        String result = jsonObj.toJSONString();
        return result;
    }

    private <T> JSONObject getJsonObject(T object) {
        JSONObject jsonObj = new JSONObject();
        Class objectClass = object.getClass();
        List<Field> fields = new ArrayList<>();
        getAllFields(fields, objectClass);
        for (Field field : fields) {
//            if (!Modifier.isTransient(field.getModifiers())) {
                if (field.getType().isPrimitive() || field.getType().getSimpleName().equals("String")) {
                    jsonObj.put(field.getName(), getFieldValue(object, field.getName()));
                } else if (field.getType().isArray()) {
                    Object array = getFieldValue(object, field.getName());
                    if (array != null) {
                        JSONArray jsonArray = getJsonArray(array);
                        jsonObj.put(field.getName(), jsonArray);
                    } else {
                        jsonObj.put(field.getName(), null);
                    }
                } else {
                    if (getFieldValue(object, field.getName()) != null) {
                        jsonObj.put(field.getName(), getJsonObject(getFieldValue(object, field.getName())));
                    } else {
                        jsonObj.put(field.getName(), null);
                    }
                }
//            }
        }
        return jsonObj;
    }

    private JSONArray getJsonArray(Object object) {
        JSONArray jsonArray = new JSONArray();
        //для ArrayList и т.п. здесь передается массив определенного размера, даже если в самом списке нет столько элементов
        for (int i = 0; i < Array.getLength(object); i++) {
            Object arrayElement = Array.get(object, i);

            if (arrayElement != null) {
                if (arrayElement.getClass().isPrimitive() || arrayElement.getClass().getSimpleName().equals("String")) {
                    jsonArray.add(arrayElement);
                } else if (arrayElement.getClass().isArray()) {
                    jsonArray.add(getJsonArray(arrayElement));
                } else {
                    jsonArray.add(getJsonObject(arrayElement));
                }
            } else {
                jsonArray.add(null);
            }
        }
        return jsonArray;
    }

    //занесение всех полей (включая унаследованные) в лист полей
    private void getAllFields(List<Field> fields, Class<?> objectClass) {
        fields.addAll(Arrays.asList(objectClass.getDeclaredFields()));
        if (objectClass.getSuperclass() != null) {
            fields.addAll(Arrays.asList(objectClass.getSuperclass().getDeclaredFields()));
        }
        Iterator<Field> iterator = fields.iterator();
        while (iterator.hasNext()) {
            if (Modifier.isStatic(iterator.next().getModifiers())) {
                iterator.remove();
            }
        }
    }

    //получение самого поля, даже если оно объявлено в родителе (не значения)
    private Field getField(Class<?> objClass, String name) {
        Field field = null;
        try {
            field = objClass.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            if (objClass.getSuperclass() != null) {
                field = getField(objClass.getSuperclass(), name);
            }
        }
        return field;
    }

    //получение значения поля
    private Object getFieldValue(Object object, String name) {
        Field field = null;
        boolean isAccessible = true;
        try {
            field = getField(object.getClass(), name); //getField() for public fields
            isAccessible = field.canAccess(object);
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (field != null && !isAccessible) {
                field.setAccessible(false);
            }
        }
        return null;
    }

    @Override
    public <T> T readFromJson(String json, Class<T> objectClass) {
        //создаю новый объект переданного класса и задаю в нем значения полей из json
        //задание полей через рефлекшн (т.к. поля могут быть private и без сеттеров)
        return null;
    }
}
