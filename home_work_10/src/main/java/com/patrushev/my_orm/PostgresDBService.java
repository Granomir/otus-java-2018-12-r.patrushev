package com.patrushev.my_orm;

import com.patrushev.my_orm.dbcommon.ConnectionHelper;

import java.sql.*;
import java.util.StringJoiner;

//сделать всё статическим и передавать в методы connection?
public class PostgresDBService implements DBService {

    private static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS otus.otus_user;";

    public Connection getCurrentConnection() {
        return connection;
    }

    private Connection connection;

    public PostgresDBService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String getMetaData() throws SQLException {
        final StringJoiner joiner = new StringJoiner("\n");
        joiner.add("Autocommit: " + connection.getAutoCommit());
        final DatabaseMetaData metaData = connection.getMetaData();
        joiner.add("DB name: " + metaData.getDatabaseProductName());
        joiner.add("DB version: " + metaData.getDatabaseProductVersion());
        joiner.add("Driver name: " + metaData.getDriverName());
        joiner.add("Driver version: " + metaData.getDriverVersion());
        joiner.add("JDBC version: " + metaData.getJDBCMajorVersion() + '.' + metaData.getJDBCMinorVersion());
        return joiner.toString();
    }

    /**
     * создает новую таблицу в БД
     * @param createTableQuery - SQL-запрос на создание новой таблицы
     * @throws SQLException
     */
    @Override
    public void createTable(String createTableQuery) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
        }
    }

    @Override
    public void deleteTables() throws SQLException {
        try (final Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROP_TABLE_USER);
        }
    }

    @Override
    public boolean checkTableAvailability(String tableName) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='public';");
            while (resultSet.next()) {
                if (resultSet.getString(1).equals(tableName)) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * создает новую БД на сервере Postgres и заменяет старое подключение на новое - к этой новой БД
     * @param dbName - имя новой БД
     */
    @Override
    public void createDB(String dbName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE " + dbName);
            //нужно ли здесь, перед тем как обновлять коннекшн, закрыть старый коннекшн? т.е. написать connection.close()
            Thread.sleep(500);
            connection = ConnectionHelper.getPostgresqlConnection(dbName);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
