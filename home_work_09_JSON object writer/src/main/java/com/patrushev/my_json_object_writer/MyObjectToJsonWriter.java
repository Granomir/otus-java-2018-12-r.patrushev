package com.patrushev.my_json_object_writer;

import com.patrushev.my_json_object_writer.utils.ReflectionHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
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
            if (object instanceof String || object instanceof Character) {
                return "\"" + object.toString() + "\"";
            } else {
                return getJsonObject(object).toString();
            }
        } else {
            return null;
        }
    }

    private Object getJsonObject(Object object) {
        if (object instanceof Collection) {
            return getJsonCollection((Collection) object);
        } else if (object instanceof Map) {
            return getJsonMap((Map) object);
        } else if (ReflectionHelper.isWrapperType(object)) {
            return object;
        } else if (object instanceof String) {
            return object;
        } else if (object.getClass().isArray()) {
            return getJsonArray(object);
        } else {
            JSONObject jsonObject = getCustomJsonObject(object);
            return Objects.requireNonNullElse(jsonObject, "");
        }
    }

    private Object getJsonCollection(Collection object) {
        JSONArray jsonArray = new JSONArray();
        for (Object o : object) {
            if (o != null) {
                jsonArray.add(getJsonObject(o));
            } else {
                jsonArray.add(null);
            }
        }
        return jsonArray;
    }

    private Object getJsonMap(Map object) {
        JSONObject jsonObject = new JSONObject();
        for (Object o : object.keySet()) {
            Object o1 = object.get(o);
            if (o1 != null) {
                jsonObject.put(o, getJsonObject(o1));
            }
        }
        return jsonObject;
    }

    private Object getJsonArray(Object object) {
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
    }

    private <T> JSONObject getCustomJsonObject(T object) {
        JSONObject jsonObj = new JSONObject();
        List<Field> fields = ReflectionHelper.getAllFields(object);
        for (Field field : fields) {
            Object fieldValue = ReflectionHelper.getFieldValue(object, field.getName());
            if (fieldValue != null) {
                jsonObj.put(field.getName(), getJsonObject(fieldValue));
            }
        }
        return jsonObj;
    }


}
