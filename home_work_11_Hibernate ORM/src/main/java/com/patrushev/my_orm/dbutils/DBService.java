package com.patrushev.my_orm.dbutils;

import com.patrushev.my_orm.data.DataSet;

public interface DBService extends AutoCloseable {

    <T extends DataSet> void save(T entity);

    <T extends DataSet> T load(long id, Class<T> clazz);
}
