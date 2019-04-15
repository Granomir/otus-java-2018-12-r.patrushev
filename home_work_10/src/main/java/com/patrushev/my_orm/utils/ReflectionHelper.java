package com.patrushev.my_orm.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ReflectionHelper {
    private static Set<Class<?>> wrappers = new HashSet<>();

    static {
        wrappers.add(Boolean.class);
        wrappers.add(Byte.class);
        wrappers.add(Short.class);
        wrappers.add(Integer.class);
        wrappers.add(Long.class);
        wrappers.add(Float.class);
        wrappers.add(Double.class);
        wrappers.add(Void.class);
    }


    /**
     * возвращает список полей, объявленных в классе переданного объекта
     */
    public static List<Field> getAllDeclaredFields(Object object) {
        Class<?> objectClass = object.getClass();
        List<Field> fields = new ArrayList<>(Arrays.asList(objectClass.getDeclaredFields()));
        fields.removeIf(field -> Modifier.isStatic(field.getModifiers()));
        return fields;
    }

    /**
     * возвращает список полей, объявленных в классе переданного объекта
     */
    public static List<Field> getAllDeclaredFieldsFromClass(Class clazz) {
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        fields.removeIf(field -> Modifier.isStatic(field.getModifiers()));
        return fields;
    }

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
     * возвращает список всех полей переданного класса (включая унаследованные)
     */
    public static List<Field> getAllFieldsFromClass(Class clazz) {
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != null) {
            fields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        }
        fields.removeIf(field -> Modifier.isStatic(field.getModifiers()));
        return fields;
    }

    /**
     * возвращает поля, объявленные в классе переданного объекта, с их значениями
     */
    public static Map<String, Object> getDeclaredFieldsAndValues(Object entity) {
        Map<String, Object> fieldsAndValues = new HashMap<>();
        List<Field> fields = ReflectionHelper.getAllDeclaredFields(entity);
        for (Field field : fields) {
            fieldsAndValues.put(field.getName(), ReflectionHelper.getFieldValue(entity, field.getName()));
        }
        return fieldsAndValues;
    }

    /**
     * возвращает объект запрашиваемого по имени поля
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
     * возвращает конкретное значение поля переданного объекта
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

    /**
     * Определяет, является ли переданный объект примитивным типом в обёртке
     */
    public static boolean isWrapperType(Object obj) {
        return wrappers.contains(obj.getClass());
    }

    /**
     * присваивает переданное значение конкретного поля переданного объекта
     * @param entity - редактируемый объект
     * @param field - поле, которому присваивается значение
     * @param value - присваиваемое значение
     */
    public static void setFieldValue(Object entity, Field field, Object value) throws IllegalAccessException {
        boolean isAccessible = field.canAccess(entity);
        if (isAccessible) {
            field.set(entity, value);
        } else {
            try {
                field.setAccessible(true);
                field.set(entity, value);
            } finally {
                field.setAccessible(false);
            }
        }
    }
}