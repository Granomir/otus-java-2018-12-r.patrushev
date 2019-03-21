package com.patrushev.my_json_object_writer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class test {
    public static void main(String[] args) {
//        //объект для сериализации
//        Ford car = new Ford();
//        //получаем класс объекта
//        Class<? extends Car> aClass = car.getClass();
//        //получаем все поля объекта (кроме статических), включая унаследованные
//        List<Field> fields = new ArrayList<>();
//        getAllFields(fields, car.getClass());
//        //печатаем все полученные поля объекта и их значения
//        System.out.println(fields.size());
//        for (Field field : fields) {
//            System.out.println(field.getName() + " : " + getFieldValue(car, field.getName()));
//        }

        MyObjectToJsonWriter test = new MyObjectToJsonWriter();
        Ford ford = new Ford();
        System.out.println(test.writeToJson(ford));
        System.out.println();
        String[] a = new String[10];
        for (int i = 0; i < a.length; i++) {
            a[i] = String.valueOf(i);

        }
        System.out.println(test.writeToJson(a));
    }

    //занесение всех полей (включая унаследованные) в лист полей
    static void getAllFields(List<Field> fields, Class<?> objectClass) {
        fields.addAll(Arrays.asList(objectClass.getDeclaredFields()));
        if (objectClass.getSuperclass() != null) {
            fields.addAll(Arrays.asList(objectClass.getSuperclass().getDeclaredFields()));
        }
        Iterator<Field> iterator = fields.iterator();
        while (iterator.hasNext()) {
            if(Modifier.isStatic(iterator.next().getModifiers())) {
                iterator.remove();
            }
        }
    }

    //получение самого поля, даже если оно объявлено в родителе (не значения)
    static Field getField(Class<?> objClass, String name) {
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
    static Object getFieldValue(Object object, String name) {
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
}
