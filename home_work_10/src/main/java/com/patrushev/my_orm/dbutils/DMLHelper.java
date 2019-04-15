package com.patrushev.my_orm.dbutils;

import com.patrushev.my_orm.data.DataSet;

public interface DMLHelper {
    <T> String getInsertQuery(T entity);
}
