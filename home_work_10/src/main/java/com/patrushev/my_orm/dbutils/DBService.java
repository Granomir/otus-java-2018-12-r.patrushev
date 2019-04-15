package com.patrushev.my_orm.dbutils;

import com.patrushev.my_orm.data.DataSet;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBService {

    void createTable(String createTableQuery) throws SQLException;

    void dropDB(String dbName) throws SQLException;

    boolean checkTableAvailability(String tableName);

    void createDB(String dbName);

    Connection getCurrentConnection();

    <T extends DataSet> void save(T entity) throws SQLException;
}
