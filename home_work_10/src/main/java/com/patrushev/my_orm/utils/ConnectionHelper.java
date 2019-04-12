package com.patrushev.my_orm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {
    private static Logger logger = LoggerFactory.getLogger(ConnectionHelper.class);
    private static String currentDB = "postgres";

    /**
     * @return подключение к базе данных, имя которой записано в currentDB
     */
    public static Connection getPostgresqlConnection() throws SQLException {
        final String connString = "jdbc:postgresql://" +    // db type
                "localhost:" +                              // host name
                "5432/" +                                   // port
                currentDB + "?" +                           // db name
                "user=postgres";                            // login
        logger.info("Создано подключение к БД " + currentDB + " по запросу: " + connString);
        return DriverManager.getConnection(connString);
    }

    /**
     * изменяет значение currentDB для подключения к другой БД
     *
     * @param dbName - имя новой БД
     * @return подключение к базе данных, имя которой записано в currentDB
     */
    public static Connection getPostgresqlConnection(String dbName) throws SQLException {
        currentDB = dbName;
        return getPostgresqlConnection();
    }
}
