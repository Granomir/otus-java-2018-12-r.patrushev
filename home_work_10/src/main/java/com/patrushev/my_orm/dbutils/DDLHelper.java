package com.patrushev.my_orm.dbutils;

import com.patrushev.my_orm.data.DataSet;

public interface DDLHelper {
    String getCreateTableQuery(Class<? extends DataSet> entityClass);
}
