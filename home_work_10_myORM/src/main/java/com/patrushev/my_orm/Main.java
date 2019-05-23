package com.patrushev.my_orm;

import com.patrushev.my_orm.data.DataSet;
import com.patrushev.my_orm.data.DataSetDAO;
import com.patrushev.my_orm.data.UserDataSet;
import com.patrushev.my_orm.dbutils.QueryingHelper;
import com.patrushev.my_orm.dbutils.PostgresQueryingHelper;
import com.patrushev.my_orm.utils.ConnectionHelper;
import com.patrushev.my_orm.dbutils.DBService;
import com.patrushev.my_orm.dbutils.PostgresDBService;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        try (Connection postgresConnection = ConnectionHelper.getPostgresqlConnection()){
            DataSet savingUser1 = new UserDataSet("Roman", 29);
            DataSet savingUser2 = new UserDataSet("Tatiana", 28);
            DataSet savingUser3 = new UserDataSet("Anna", 4);
            DataSet savingUser4 = new UserDataSet("Aleksandra", 3);
            QueryingHelper postgresQueryingHelper = new PostgresQueryingHelper();
            DataSetDAO dataSetDAO = new DataSetDAO(postgresQueryingHelper);
            DBService postgresDBService = new PostgresDBService(postgresConnection, postgresQueryingHelper, dataSetDAO);
            postgresDBService.save(savingUser1);
            postgresDBService.save(savingUser2);
            postgresDBService.save(savingUser3);
            postgresDBService.save(savingUser4);
            DataSet loadedUser1 = postgresDBService.load(1, UserDataSet.class);
            DataSet loadedUser2 = postgresDBService.load(2, UserDataSet.class);
            DataSet loadedUser3 = postgresDBService.load(3, UserDataSet.class);
            DataSet loadedUser4 = postgresDBService.load(4, UserDataSet.class);
            System.out.println(loadedUser1.equals(savingUser1));
            System.out.println(loadedUser2.equals(savingUser2));
            System.out.println(loadedUser3.equals(savingUser3));
            System.out.println(loadedUser4.equals(savingUser4));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
