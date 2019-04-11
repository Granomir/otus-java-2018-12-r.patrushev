package com.patrushev.my_orm.executors;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Executor {

    public static <T> T query(Connection connection, String query, TResultHandler<T> handler, Class<T> clazz) throws SQLException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        try (Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            return handler.handle(clazz, result);
        }
    }

    public static void update(Connection connection, String update) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(update);
        }
    }
}
