package com.patrushev.my_orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Executor {

    public static ResultSet query(Connection connection, String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            return statement.executeQuery(query);
        }
    }

    public static void update(Connection connection, String update) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(update);
        }
    }
}
