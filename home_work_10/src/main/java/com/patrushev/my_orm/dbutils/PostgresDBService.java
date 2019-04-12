package com.patrushev.my_orm.dbutils;

import com.patrushev.my_orm.utils.ConnectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class PostgresDBService implements DBService {
    private static Logger logger = LoggerFactory.getLogger(PostgresDBService.class);
    private Connection connection;

    /**
     * возвращает актуальное подключение к БД (к БД с определенным именем)
     */
    public Connection getCurrentConnection() {
        return connection;
    }

    public PostgresDBService(Connection connection) {
        this.connection = connection;
    }

    /**
     * создает новуб таблицу в БД, согласно переданному SQL-запросу
     */
    @Override
    public void createTable(String createTableQuery) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
            logger.info("Создана новая таблица в БД по запросу: " + createTableQuery);
            Thread.sleep(500);
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * удаляет БД с переданным именем
     */
    @Override
    public void dropDB(String dbName) throws SQLException {
        //по идее тут нужно закрыть старое соединение, т.к. оно больше не понадобится???
        connection.close();
        logger.info("Старое соединение закрыто");
        try (Connection con = ConnectionHelper.getPostgresqlConnection("postgres"); Statement statement = con.createStatement()) {
            statement.executeUpdate("DROP DATABASE IF EXISTS " + dbName + ";");
            logger.info("Удалена БД " + dbName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * проверка наличия таблицы с переданным именем в БД
     */
    @Override
    public boolean checkTableAvailability(String tableName) {
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='public';")) {
            while (resultSet.next()) {
                if (resultSet.getString(1).equals(tableName.toLowerCase())) {
                    logger.info("Таблица " + tableName + " присутствует в БД");
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * создает новую БД на сервере Postgres и заменяет старое подключение на новое - к этой новой БД
     */
    @Override
    public void createDB(String dbName) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE " + dbName);
            logger.info("БД " + dbName + " создана");
            Thread.sleep(500);
            //по идее тут нужно закрыть старое соединение, т.к. оно больше не понадобится???
            connection.close();
            logger.info("Старое соединение закрыто");
            connection = ConnectionHelper.getPostgresqlConnection(dbName);
            logger.info("Полученно новое соединение к только что созданной БД");
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
