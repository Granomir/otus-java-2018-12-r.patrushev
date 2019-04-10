package com.patrushev.my_orm;

import com.patrushev.my_orm.dbcommon.ConnectionHelper;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        //подготовка БД
        String createTableQuery = "CREATE TABLE IF NOT EXISTS userdataset (\n" +
                "  id        BIGSERIAL NOT NULL PRIMARY KEY,\n" +
                "  user_name VARCHAR(255),\n" +
                "  age       INTEGER\n" +
                ");";
        DBService postgresDbService = new PostgresDBService(ConnectionHelper.getPostgresqlConnection());
        postgresDbService.createDB("mydb");
        postgresDbService.createTable(createTableQuery);
        //вставка новых объектов в БД
        DataSetDAO dao = new DataSetDAO(postgresDbService);
        dao.save(new UserDataSet("roman", 29));
        dao.save(new UserDataSet("tatiana", 28));
        dao.save(new UserDataSet("anna", 4));
        dao.save(new UserDataSet("alexandra", 3));
        //выгрузка элементов из БД
        UserDataSet user1 = dao.load(1, UserDataSet.class);
        UserDataSet user2 = dao.load(2, UserDataSet.class);
        UserDataSet user3 = dao.load(3, UserDataSet.class);
        UserDataSet user4 = dao.load(4, UserDataSet.class);
    }
}
