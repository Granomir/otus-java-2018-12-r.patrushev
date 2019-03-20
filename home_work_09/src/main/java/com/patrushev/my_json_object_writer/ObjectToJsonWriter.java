package com.patrushev.my_json_object_writer;

public interface ObjectToJsonWriter {
    <T> String writeToJson(T object);

    <T> T readFromJson(String json, Class<T> objectClass);
}
