package com.patrushev.my_json_object_writer.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionHelper {

    /**
     * возвращает список всех полей переданного объекта (включая унаследованные)
     */
    public static List<Field> getAllFields(Object object) {
        Class<?> objectClass = object.getClass();
        List<Field> fields = new ArrayList<>(Arrays.asList(objectClass.getDeclaredFields()));
        if (objectClass.getSuperclass() != null) {
            fields.addAll(Arrays.asList(objectClass.getSuperclass().getDeclaredFields()));
        }
        fields.removeIf(field -> Modifier.isStatic(field.getModifiers()));
        return fields;
    }

    /**
     * возвращает объект запрашиваемого поля
     *
     * @param objClass - интересующий класс
     * @param name     - имя интересующего поля
     */
    public static Field getField(Class<?> objClass, String name) {
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

    /**
     * возвращает конкретное значение поля экземпляра
     *
     * @param object - объект некого класса
     * @param name   - имя интересующего поля
     */
    public static Object getFieldValue(Object object, String name) {
        Field field = null;
        boolean isAccessible = true;
        try {
            field = getField(object.getClass(), name);
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