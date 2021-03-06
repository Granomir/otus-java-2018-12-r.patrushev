package com.patrushev.home_work_02.factories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ObjectFactoryByCloning implements ObjectFactory {
    Gson gsonTo = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    Gson gsonFrom = new Gson();
    String objectInJson;
    Class objectClass;


    public ObjectFactoryByCloning(Object objectForClonning) {
        objectInJson = gsonTo.toJson(objectForClonning);
        objectClass = objectForClonning.getClass();
    }

    @Override
    public Object createObject() {
        return gsonFrom.fromJson(objectInJson, objectClass);
    }
}
