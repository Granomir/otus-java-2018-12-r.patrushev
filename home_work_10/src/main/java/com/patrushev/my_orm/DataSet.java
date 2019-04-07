package com.patrushev.my_orm;

//абстракция сущностей, хранящихся в БД - строка в БД
public abstract class DataSet {
    //почему id здесь????
    private long id;

    public long getId() {
        return id;
    }
}
