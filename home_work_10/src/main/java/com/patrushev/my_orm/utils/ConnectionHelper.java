package com.patrushev.my_orm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionHelper {
    private static Logger logger = LoggerFactory.getLogger(ConnectionHelper.class);

    /**
     * @return подключение к базе данных
     */
    public static Connection getPostgresqlConnection() throws SQLException {
        String connString = null;
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            connString = "jdbc:" + prop.getProperty("db.type") + "://" +    // db type
                    prop.getProperty("db.host") + ":" +                             // host name
                    prop.getProperty("db.port") + "/" +                                  // port
                    prop.getProperty("db.name") + "?" +                               // db name
                    "user=" + prop.getProperty("db.login");                            // login
            logger.info("Создано подключение к БД postgres запросом " + connString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert connString != null;
        return DriverManager.getConnection(connString);
    }
}
