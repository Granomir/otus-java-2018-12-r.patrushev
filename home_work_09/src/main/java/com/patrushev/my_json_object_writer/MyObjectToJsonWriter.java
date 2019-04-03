package com.patrushev.my_json_object_writer;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@SuppressWarnings("unchecked")
public class MyObjectToJsonWriter implements ObjectToJsonWriter {

    private Set<Class<?>> wrappers = new HashSet<>();

    {
        wrappers.add(Boolean.class);
        wrappers.add(Character.class);
        wrappers.add(Byte.class);
        wrappers.add(Short.class);
        wrappers.add(Integer.class);
        wrappers.add(Long.class);
        wrappers.add(Float.class);
        wrappers.add(Double.class);
        wrappers.add(Void.class);
    }

    /**
     * @param object - сериализуемый объект
     * @param <T>    - тип сериализуемого объекта
     * @return - сериализованный объект (строка) в формате JSON
     */
    @Override
    public <T> String writeToJson(T object) {
        if (object != null) {
            if (isWrapperType(object)) {
                return object.toString();
            } else if (object instanceof String) {
                return "\"" + object.toString() + "\"";
            } else {
                return ((JSONAware) getJsonObject(object)).toJSONString();
            }
        } else {
            return null;
        }
    }


    private Object getJsonObject(Object object) {
        if (object instanceof Collection) {
            Collection tempCollection = (Collection) object;
            JSONArray jsonArray = new JSONArray();
            for (Object o : tempCollection) {
                if (o != null) {
                    jsonArray.add(getJsonObject(o));
                } else {
                    jsonArray.add(null);
                }
            }
            return jsonArray;
        } else if (object instanceof Map) {
            Map tempMap = (Map) object;
            JSONObject jsonObject = new JSONObject();
            for (Object o : tempMap.keySet()) {
                Object o1 = tempMap.get(o);
                if (o1 != null) {
                    jsonObject.put(o, getJsonObject(o1));
                }
            }
            return jsonObject;
        } else if (isWrapperType(object)) {
            return object;
        } else if (object instanceof String) {
            return object;
        } else if (object.getClass().isArray()) {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < Array.getLength(object); i++) {
                Object arrayElement = Array.get(object, i);
                if (arrayElement != null) {
                    jsonArray.add(getJsonObject(arrayElement));
                } else {
                    jsonArray.add(null);
                }
            }
            return jsonArray;
        } else {
            JSONObject jsonObject = getCustomJsonObject(object);
            return Objects.requireNonNullElse(jsonObject, "");
        }
    }


    /**
     * Определяет, является ли аргумент примитивным типом в обёртке
     */
    private boolean isWrapperType(Object obj) {
        return wrappers.contains(obj.getClass());
    }

    private <T> JSONObject getCustomJsonObject(T object) {
        JSONObject jsonObj = new JSONObject();
        List<Field> fields = getAllFields(object);
        for (Field field : fields) {
            Object fieldValue = getFieldValue(object, field.getName());
            if (fieldValue != null) {
                jsonObj.put(field.getName(), getJsonObject(fieldValue));
            }
        }
        return jsonObj;
    }

    /**
     * возвращает список всех полей переданного объекта (включая унаследованные)
     */
    private List<Field> getAllFields(Object object) {
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

    /**
     * возвращает конкретное значение поля экземпляра
     *
     * @param object - объект некого класса
     * @param name   - имя интересующего поля
     */
    private Object getFieldValue(Object object, String name) {
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
