package com.patrushev.my_orm;

import com.patrushev.my_orm.dbcommon.ConnectionHelper;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        //подготовка БД
        String createTableQuery = "CREATE TABLE IF NOT EXISTS UserDataSet (\n" +
                "  id        BIGSERIAL NOT NULL PRIMARY KEY,\n" +
                "  user_name VARCHAR(255),\n" +
                "  age       INTEGER\n" +
                ");";
        DBService postgresDbService = new PostgresDBService(ConnectionHelper.getPostgresqlConnection());
        postgresDbService.createDB("myDB");
        postgresDbService.createTable(createTableQuery);
        //вставка новых объектов в БД
        DataSetDAO dao = new DataSetDAO(postgresDbService);
        dao.save(new UserDataSet("roman", 29));
        dao.save(new UserDataSet("tatiana", 28));
        dao.save(new UserDataSet("anna", 4));
        dao.save(new UserDataSet("alexandra", 3));

    }
}
