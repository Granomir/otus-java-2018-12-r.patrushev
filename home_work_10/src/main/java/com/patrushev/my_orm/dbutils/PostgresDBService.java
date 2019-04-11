package com.patrushev.my_orm.dbutils;

import com.patrushev.my_orm.utils.ConnectionHelper;

import java.sql.*;

public class PostgresDBService implements DBService {

    private Connection connection;

    /**
     * возвращает актуальное подключение к БД (к БД с определенным именем)
     * @return
     */
    public Connection getCurrentConnection() {
        return connection;
    }

    public PostgresDBService(Connection connection) {
        this.connection = connection;
    }

    /**
     * создает новуб таблицу в БД, согласно переданному SQL-запросу
     * @param createTableQuery
     */
    @Override
    public void createTable(String createTableQuery) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
            Thread.sleep(500);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * удаляет БД с переданным именем
     * @param dbName
     * @throws SQLException
     */
    @Override
    public void dropDB(String dbName) throws SQLException {
        connection.close();
        try (Connection con = ConnectionHelper.getPostgresqlConnection("postgres"); Statement statement = con.createStatement()) {
            statement.executeUpdate("DROP DATABASE IF EXISTS " + dbName + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * проверка наличия таблицы с переданным именем в БД
     * @param tableName
     * @return
     */
    @Override
    public boolean checkTableAvailability(String tableName) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='public';");
            while (resultSet.next()) {
                if (resultSet.getString(1).equals(tableName.toLowerCase())) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * создает новую БД на сервере Postgres и заменяет старое подключение на новое - к этой новой БД
     * @param dbName
     */
    @Override
    public void createDB(String dbName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE " + dbName);
            Thread.sleep(500);
            connection = ConnectionHelper.getPostgresqlConnection(dbName);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
