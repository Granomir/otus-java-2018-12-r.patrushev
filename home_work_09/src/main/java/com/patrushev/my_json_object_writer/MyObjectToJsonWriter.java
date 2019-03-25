package com.patrushev.my_json_object_writer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@SuppressWarnings("unchecked")
public class MyObjectToJsonWriter implements ObjectToJsonWriter {

    private Set<Class<?>> ret = new HashSet<>();

    {
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
    }

    /**
     * @param object - сериализуемый объект
     * @param <T>    - тип сериализуемого объекта
     * @return - сериализованный объект (строка) в формате JSON
     */
    @Override
    public <T> String writeToJson(T object) {
        if (object != null) {
            String result;
            if (object instanceof Collection) {
                result = getJsonCollection(object).toJSONString();
            } else if (object instanceof Map) {
                result = getJsonMap(object).toJSONString();
            } else if (isWrapperType(object)) {
                result = object.toString();
            } else if (object instanceof String) {
                result = "\"" + object.toString() + "\"";
            } else if (object.getClass().isArray()) {
                result = getJsonArray(object).toJSONString();
            } else {
                JSONObject jsonObject = getJsonObject(object);
                if (jsonObject != null) {
                    result = jsonObject.toJSONString();
                } else {
                    result = "";
                }
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * Определяет, является ли аргумент примитивным типом в обёртке
     */
    private boolean isWrapperType(Object obj) {
        return ret.contains(obj.getClass());
    }

    private <T> JSONObject getJsonMap(T object) {
        Map tempMap = (Map) object;
        JSONObject jsonObject = new JSONObject();
        for (Object o : tempMap.keySet()) {
            Object o1 = tempMap.get(o);
            if (o1 != null) {
                if (o1 instanceof Collection) {
                    jsonObject.put(o, getJsonCollection(o1));
                } else if (o1 instanceof Map) {
                    jsonObject.put(o, getJsonMap(o1));
                } else if (isWrapperType(o1)) {
                    jsonObject.put(o, o1);
                } else if (o1.getClass().isArray()) {
                    jsonObject.put(o, getJsonArray(o1));
                } else if (o1 instanceof String) {
                    jsonObject.put(o, o1);
                } else {
                    JSONObject tempJsonObject = getJsonObject(o1);
                    if (tempJsonObject != null) {
                        jsonObject.put(o, tempJsonObject);
                    }
                }
            }
        }
        return jsonObject;
    }

    private <T> JSONArray getJsonCollection(T object) {
        Collection tempCollection = (Collection) object;
        JSONArray jsonArray = new JSONArray();
        for (Object o : tempCollection) {
            if (o != null) {
                if (o instanceof Collection) {
                    jsonArray.add(getJsonCollection(o));
                } else if (o instanceof Map) {
                    jsonArray.add(getJsonMap(o));
                } else if (isWrapperType(o)) {
                    jsonArray.add(o);
                } else if (o.getClass().isArray()) {
                    jsonArray.add(getJsonArray(o));
                } else if (o instanceof String) {
                    jsonArray.add(o);
                } else {
                    JSONObject jsonObject = getJsonObject(o);
                    if (jsonObject != null) {
                        jsonArray.add(jsonObject);
                    }
                }
            } else {
                jsonArray.add(null);
            }
        }
        return jsonArray;
    }


    private <T> JSONObject getJsonObject(T object) {
        if (object != null) {
            JSONObject jsonObj = new JSONObject();
            List<Field> fields = getAllFields(object);
            for (Field field : fields) {
                Object fieldValue = getFieldValue(object, field.getName());
                if (fieldValue != null) {
                    if (field.getType().isPrimitive() || field.getType().getSimpleName().equals("String")) {
                        jsonObj.put(field.getName(), fieldValue);
                    } else if (field.getType().isArray()) {
                        JSONArray jsonArray = getJsonArray(fieldValue);
                        jsonObj.put(field.getName(), jsonArray);
                    } else if (fieldValue instanceof Collection) {
                        jsonObj.put(field.getName(), getJsonCollection(fieldValue));
                    } else if (fieldValue instanceof Map) {
                        jsonObj.put(field.getName(), getJsonMap(fieldValue));
                    } else {
                        JSONObject jsonObject = getJsonObject(fieldValue);
                        if (jsonObject != null) {
                            jsonObj.put(field.getName(), jsonObject);
                        }
                    }
                }
            }
            return jsonObj;
        } else {
            return null;
        }
    }

    private <T> JSONArray getJsonArray(T object) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < Array.getLength(object); i++) {
            Object arrayElement = Array.get(object, i);
            if (arrayElement != null) {
                if (isWrapperType(arrayElement) || arrayElement.getClass().getSimpleName().equals("String")) {
                    jsonArray.add(arrayElement);
                } else if (arrayElement.getClass().isArray()) {
                    jsonArray.add(getJsonArray(arrayElement));
                } else {
                    JSONObject jsonObject = getJsonObject(arrayElement);
                    if (jsonObject != null) {
                        jsonArray.add(jsonObject);
                    }
                }
            } else {
                jsonArray.add(null);
            }
        }
        return jsonArray;
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
      * @param objClass - интересующий класс
     * @param name - имя интересующего поля
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
     * @param object - объект некого класса
     * @param name - имя интересующего поля
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
