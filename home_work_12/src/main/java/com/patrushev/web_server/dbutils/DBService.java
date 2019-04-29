package com.patrushev.web_server.dbutils;

import com.patrushev.web_server.data.DataSet;

public interface DBService extends AutoCloseable {

    <T extends DataSet> void save(T entity);

    <T extends DataSet> T load(long id, Class<T> clazz);
}
