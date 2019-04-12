package com.patrushev.my_orm.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Executor {
    private static Logger logger = LoggerFactory.getLogger(Executor.class);

    public static <T> T query(Connection connection, String query, TResultHandler<T> handler, Class<T> clazz) throws SQLException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        try (Statement statement = connection.createStatement(); ResultSet result = statement.executeQuery(query)) {
            logger.info("Выполнен читающий SQL-запрос в БД: " + query);
            //я правильно понял, что после завершения метода handle() result закроется автоматически?
            return handler.handle(clazz, result);
        }
    }

    public static void update(Connection connection, String update) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            logger.info("Выполнен изменяющий SQL-запрос в БД: " + update);
            statement.executeUpdate(update);
        }
    }
}
