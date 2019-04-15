package com.patrushev.my_orm.dbutils;

public class PostgresDMLHelper implements DMLHelper {
    @Override
    public <T> String getInsertQuery(T entity) {
        StringBuilder insertQuery = new StringBuilder();
        insertQuery.append("INSERT INTO ").append(entity.getClass().getSimpleName()).append(" (");

        return null;
    }
}
