package com.patrushev.my_orm.dbutils;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBService {

    void createTable(String createTableQuery) throws SQLException;

    void dropDB(String dbName) throws SQLException;

    boolean checkTableAvailability(String tableName);

    void createDB(String dbName);

    Connection getCurrentConnection();
}
