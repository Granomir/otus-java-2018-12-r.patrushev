package com.patrushev.my_orm.dbutils;

import com.patrushev.my_orm.data.DataSet;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface DBService {

    <T extends DataSet> void save(T entity);

    <T extends DataSet> T load(long id, Class<T> clazz);
}
