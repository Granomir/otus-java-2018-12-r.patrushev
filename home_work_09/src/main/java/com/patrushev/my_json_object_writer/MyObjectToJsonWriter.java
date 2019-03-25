package com.patrushev.my_json_object_writer;

import com.google.gson.JsonObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@SuppressWarnings("unchecked")
public class MyObjectToJsonWriter implements ObjectToJsonWriter {


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
     * Определение того, что аргумент является примитивным типом в обёртке
     *
     * @return
     */
    private static boolean isWrapperType(Object obj) {
        Set<Class<?>> ret = new HashSet<>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret.contains(obj.getClass());
    }

    private <T> JSONObject getJsonMap(T object) {
        Map tempCollection = (Map) object;
        JSONObject jsonObject = new JSONObject();
        for (Object o : tempCollection.keySet()) {
            if (tempCollection.get(o) instanceof Collection) {
                jsonObject.put(o, getJsonCollection(tempCollection.get(o)));
            } else if (tempCollection.get(o) instanceof Map) {
                jsonObject.put(o, getJsonMap(tempCollection.get(o)));
            } else {
                JSONObject tempJsonObject = getJsonObject(tempCollection.get(o));
                if (tempJsonObject != null) {
                    jsonObject.put(o, tempJsonObject);
                }
            }
        }
        return jsonObject;
    }

    private JSONArray getJsonCollection(Object object) {
        Collection tempCollection = (Collection) object;
        JSONArray jsonArray = new JSONArray();
        for (Object o : tempCollection) {
            if (o instanceof Collection) {
                jsonArray.add(getJsonCollection(o));
            } else if (o instanceof Map) {
                jsonArray.add(getJsonMap(o));
            } else {
                JSONObject jsonObject = getJsonObject(o);
                if (jsonObject != null) {
                    jsonArray.add(jsonObject);
                }
            }
        }
        return jsonArray;
    }


    private <T> JSONObject getJsonObject(T object) {
        if (object != null) {
            JSONObject jsonObj = new JSONObject();
            Class objectClass = object.getClass();
            List<Field> fields = getAllFields(objectClass);
            for (Field field : fields) {
                if (getFieldValue(object, field.getName()) != null) {
                    if (field.getType().isPrimitive() || field.getType().getSimpleName().equals("String")) {
                        jsonObj.put(field.getName(), getFieldValue(object, field.getName()));
                    } else if (field.getType().isArray()) {
                        Object array = getFieldValue(object, field.getName());
                        if (array != null) {
                            JSONArray jsonArray = getJsonArray(array);
                            jsonObj.put(field.getName(), jsonArray);
    //                    } else {
    //                        jsonObj.put(field.getName(), null);
                        }
                    } else if (getFieldValue(object, field.getName()) instanceof Collection) {
                        jsonObj.put(field.getName(), getJsonCollection(getFieldValue(object, field.getName())));
                    } else if (getFieldValue(object, field.getName()) instanceof Map) {
                        jsonObj.put(field.getName(), getJsonMap(getFieldValue(object, field.getName())));
                    } else {
                        if (getFieldValue(object, field.getName()) != null) {
                            JSONObject jsonObject = getJsonObject(getFieldValue(object, field.getName()));
                            if (jsonObject != null) {
                                jsonObj.put(field.getName(), jsonObject);
                            }
    //                    } else {
    //                        jsonObj.put(field.getName(), null);
                        }
                    }
                }
            }
            return jsonObj;
        } else {
            return null;
        }
    }

    private JSONArray getJsonArray(Object object) {
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

    //занесение всех полей (включая унаследованные) в лист полей
    private List<Field> getAllFields(Class<?> objectClass) {
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(objectClass.getDeclaredFields()));
        if (objectClass.getSuperclass() != null) {
            fields.addAll(Arrays.asList(objectClass.getSuperclass().getDeclaredFields()));
        }
        fields.removeIf(field -> Modifier.isStatic(field.getModifiers()));
        return fields;
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
}
