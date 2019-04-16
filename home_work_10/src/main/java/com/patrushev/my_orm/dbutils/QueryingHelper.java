package com.patrushev.my_orm.dbutils;

import com.patrushev.my_orm.data.DataSet;

import java.lang.reflect.Field;
import java.util.List;

public interface QueryingHelper {
    String getCreateTableQuery(Class<? extends DataSet> entityClass);

    <T> String getInsertQuery(T entity);

    <T> String getSelectQuery(long id, Class<T> clazz);

    List<Field> getFieldList(String className);
}
