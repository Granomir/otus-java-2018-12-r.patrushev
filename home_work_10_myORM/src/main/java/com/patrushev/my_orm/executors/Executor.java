package com.patrushev.my_orm.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class Executor {
    private static Logger logger = LoggerFactory.getLogger(Executor.class);

    public static <T> T queryPrepared(PreparedStatement select, TResultHandler<T> handler, Class<T> clazz) throws SQLException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        try (select; ResultSet result = select.executeQuery()) {
            logger.info("Выполнен читающий SQL-запрос в БД: " + select);
            return handler.handle(clazz, result);
        }
    }

    public static void update(Connection connection, String update) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            logger.info("Выполнен изменяющий SQL-запрос в БД: " + update);
            statement.executeUpdate(update);
        }
    }

    public static void updatePrepared(PreparedStatement update) throws SQLException {
        try (update) {
            logger.info("Выполнен изменяющий SQL-запрос в БД: " + update);
            update.executeUpdate();
        }
    }
}
