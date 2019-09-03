import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DbExecutorImpl implements DbExecutor {
    private Logger logger = LoggerFactory.getLogger(DbExecutorImpl.class);

    @Override
    public int insertRecord(String sqlQuery, List<Object> values, Connection connection) throws SQLException {
        logger.info("start inserting record");
        Savepoint savePoint = connection.setSavepoint("savePointName");
        logger.info("savepoint saved");
        try (PreparedStatement pst = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            //TODO в этом месте заполняется только первый параметр, а второй почему то нет
            for (Object value : values) {
                setValue(pst, i, value);
                i++;
            }
            System.out.println(i);
            logger.info("prepared to execute - " + pst);
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                logger.info("finish inserting record");
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            logger.error("error occured when inserting record");
            connection.rollback(savePoint);
            logger.error("savepoint was restored");
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void updateRecord(String sqlQuery, List<Object> values, Connection connection, long idField) throws SQLException {
        logger.info("start updating record");
        Savepoint savePoint = connection.setSavepoint("savePointName");
        logger.info("savepoint saved");
        try (PreparedStatement pst = connection.prepareStatement(sqlQuery)) {
            int i = 1;
            for (Object value : values) {
                setValue(pst, i, value);
                i++;
            }
            pst.setLong(i, idField);
            logger.info("prepared to execute - " + pst);
            pst.executeUpdate();
            logger.info("finish updating record");
        } catch (SQLException ex) {
            logger.error("error occured when updating record");
            connection.rollback(savePoint);
            logger.error("savepoint was restored");
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public <T> Optional<T> selectRecord(String sqlQuery, long id, Connection connection, Function<ResultSet, T> rsHandler) throws SQLException {
        logger.info("start selecting record");
        try (PreparedStatement pst = connection.prepareStatement(sqlQuery)) {
            pst.setLong(1, id);
            logger.info("prepared to execute - " + pst);
            try (ResultSet rs = pst.executeQuery()) {
                logger.info("finish selecting record");
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        }
    }

    private void setValue(PreparedStatement pst, int i, Object value) throws SQLException {
        Class<?> valueType = value.getClass();
        System.out.println(valueType.getSimpleName());
        if (valueType.equals(Long.TYPE))
            pst.setLong(i, (long) value);
        if (valueType.equals(Integer.TYPE))
            pst.setInt(i, (int) value);
        if (valueType.equals(Short.TYPE) || valueType.equals(Byte.TYPE))
            pst.setShort(i, (short) value);
        if (valueType.equals(Double.TYPE))
            pst.setDouble(i, (double) value);
        if (valueType.equals(Float.TYPE))
            pst.setFloat(i, (float) value);
        if (valueType.equals(Character.TYPE) || valueType.equals(String.class))
            pst.setString(i, (String) value);
        System.out.println("param = " + i + " value " + value);
    }
}
