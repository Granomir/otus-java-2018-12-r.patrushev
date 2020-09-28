package dbservice.impl;

import dbservice.DbExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test_datasorce.DataSource;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DbExecutorImpl implements DbExecutor {
    private final Logger logger = LoggerFactory.getLogger(DbExecutorImpl.class);
    private final DataSource dataSource;

    public DbExecutorImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int insertRecord(String sqlQuery, List<Object> values) throws SQLException {
        logger.info("start inserting record");
        try (Connection connection = dataSource.getConnection()) {
            Savepoint savePoint = connection.setSavepoint("savePointName");
            logger.info("savepoint saved");
            try (PreparedStatement pst = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
                int i = 1;
                for (Object value : values) {
                    setValue(pst, i, value);
                    i++;
                }
                logger.info("prepared to execute - {}", pst);
                pst.executeUpdate();
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    rs.next();
                    logger.info("finish inserting record");
                    connection.commit();
                    return rs.getInt(1);
                }
            } catch (SQLException ex) {
                logger.error("error occurred when inserting record", ex);
                connection.rollback(savePoint);
                logger.error("savepoint was restored");
                throw ex;
            }
        }
    }

    @Override
    public void updateRecord(String sqlQuery, List<Object> values, long idField) throws SQLException {
        logger.info("start updating record");
        try (Connection connection = dataSource.getConnection()) {
            Savepoint savePoint = connection.setSavepoint("savePointName");
            logger.info("savepoint saved");
            try (PreparedStatement pst = connection.prepareStatement(sqlQuery)) {
                int i = 1;
                for (Object value : values) {
                    setValue(pst, i, value);
                    i++;
                }
                pst.setLong(i, idField);
                logger.info("prepared to execute - {}", pst);
                pst.executeUpdate();
                connection.commit();
                logger.info("finish updating record");
            } catch (SQLException ex) {
                logger.error("error occurred when updating record with id {}", idField, ex);
                connection.rollback(savePoint);
                logger.error("savepoint was restored");
                throw ex;
            }
        }
    }

    @Override
    public <T> Optional<T> selectRecord(String sqlQuery, long id, Function<ResultSet, T> rsHandler) throws SQLException {
        logger.info("start selecting record");
        try (Connection connection = dataSource.getConnection(); PreparedStatement pst = connection.prepareStatement(sqlQuery)) {
            pst.setLong(1, id);
            logger.info("prepared to execute - {}", pst);
            try (ResultSet rs = pst.executeQuery()) {
                logger.info("finish selecting record");
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        }
    }

    @Override
    public int selectRecordCount(String sqlQuery, long id) throws SQLException {
        logger.info("start selecting record count");
        try (Connection connection = dataSource.getConnection(); PreparedStatement pst = connection.prepareStatement(sqlQuery)) {
            pst.setLong(1, id);
            logger.info("prepared to execute - {}", pst);
            try (ResultSet rs = pst.executeQuery()) {
                rs.next();
                logger.info("finish selecting record count");
                return rs.getInt(1);
            }
        }
    }

    private void setValue(PreparedStatement pst, int i, Object value) throws SQLException {
        Class<?> valueType = value.getClass();
        if (valueType.equals(Long.class)) {
            pst.setLong(i, (long) value);
        } else if (valueType.equals(Integer.class)) {
            pst.setInt(i, (int) value);
        } else if (valueType.equals(Short.class) || valueType.equals(Byte.class)) {
            pst.setShort(i, (short) value);
        } else if (valueType.equals(Double.class)) {
            pst.setDouble(i, (double) value);
        } else if (valueType.equals(Float.class)) {
            pst.setFloat(i, (float) value);
        } else if (valueType.equals(Character.class) || valueType.equals(String.class)) {
            pst.setString(i, String.valueOf(value));
        }
    }
}
