package utils;

import dbservice.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class ReflectionHelper {

    /**
     * возвращает список полей, объявленных в переданном классе
     */
    public static List<Field> getAllDeclaredFieldsFromClass(Class<?> clazz) {
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        fields.removeIf(field -> Modifier.isStatic(field.getModifiers()));
        return fields;
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
     * присваивает переданное значение конкретного поля переданного объекта
     *
     * @param entity - редактируемый объект
     * @param field  - поле, которому присваивается значение
     * @param value  - присваиваемое значение
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

    public static <T> void fillFieldsNamesWithValues(T objectData, List<String> columns, List<Object> values) {
        Class<?> clazz = objectData.getClass();
        List<Field> fields = ReflectionHelper.getAllDeclaredFieldsFromClass(clazz);
        Field idField = getIdField(fields);
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!fieldName.equals(idField.getName())) {
                columns.add(fieldName);
                values.add(ReflectionHelper.getFieldValue(objectData, fieldName));
            }
        }
    }

    private static Field getIdField(List<Field> fields) {
        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null) {
                return field;
            }
        }
        throw new IllegalArgumentException("DBService может работать только с классами, имеющими поле с аннотацией \"@Id\"");
    }

    public static String getIdFieldName(Class<?> clazz) {
        final List<Field> fields = getAllDeclaredFieldsFromClass(clazz);
        final Field idField = getIdField(fields);
        return idField.getName();
    }

    public static <T> T getEmptyEntity(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<String> getAllDeclaredFieldsNamesFromClass(Class<T> clazz) {
        return getAllDeclaredFieldsFromClass(clazz).stream().map(Field::getName).collect(Collectors.toList());
    }

    public static <T> T fillEntity(Class<T> clazz, T entity, Map<String, Object> values) {
        List<Field> fields = ReflectionHelper.getAllDeclaredFieldsFromClass(clazz);
        for (Field field : fields) {
            try {
                ReflectionHelper.setFieldValue(entity, field, values.get(field.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }
}