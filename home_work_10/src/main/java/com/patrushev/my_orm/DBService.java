package com.patrushev.my_orm;

import java.sql.SQLException;

public interface DBService {
    String getMetaData() throws SQLException;

    void createTable(String createTableQuery) throws SQLException;

    void deleteTables() throws SQLException;

    boolean checkTableAvailability(String tableName);
}
