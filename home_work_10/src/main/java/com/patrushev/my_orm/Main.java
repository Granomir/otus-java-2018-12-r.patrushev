package com.patrushev.my_orm;

import com.patrushev.my_orm.data.DataSetDAO;
import com.patrushev.my_orm.data.UserDataSet;
import com.patrushev.my_orm.utils.ConnectionHelper;
import com.patrushev.my_orm.dbutils.DBService;
import com.patrushev.my_orm.dbutils.PostgresDBService;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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
        UserDataSet user1 = new UserDataSet("roman", 29);
        UserDataSet user2 = new UserDataSet("tatiana", 28);
        UserDataSet user3 = new UserDataSet("anna", 4);
        UserDataSet user4 = new UserDataSet("alexandra", 3);
        dao.save(user1);
        dao.save(user2);
        dao.save(user3);
        dao.save(user4);
        //выгрузка элементов из БД
        UserDataSet user11 = dao.load(1, UserDataSet.class);
        System.out.println("user 1 equals user 11: " + user1.equals(user11));
        UserDataSet user22 = dao.load(2, UserDataSet.class);
        System.out.println("user 2 equals user 22: " + user2.equals(user22));
        UserDataSet user33 = dao.load(3, UserDataSet.class);
        System.out.println("user 3 equals user 33: " + user3.equals(user33));
        UserDataSet user44 = dao.load(4, UserDataSet.class);
        System.out.println("user 4 equals user 44: " + user4.equals(user44));
        //удаление БД
        postgresDbService.dropDB("mydb");
    }
}
