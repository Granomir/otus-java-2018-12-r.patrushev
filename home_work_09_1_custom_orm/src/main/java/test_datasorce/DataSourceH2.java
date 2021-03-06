package test_datasorce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceH2 implements DataSource {
    private final Logger logger = LoggerFactory.getLogger(DataSourceH2.class);
    private static final String URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

    @Override
    public Connection getConnection() {
        logger.info("start getting connection to DB");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            logger.error("Error occurred while getting DB connection", e);
        }
        logger.info("finish getting connection to DB");
        return connection;
    }
}
