import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class DbExecutorImpl implements DbExecutor {

    @Override
    public int insertRecord(String sqlQuery, List<Object> values, Connection connection) throws SQLException {
        Savepoint savePoint = connection.setSavepoint("savePointName");
        try (PreparedStatement pst = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            for (Object value : values) {
                setValue(pst, i, value);
                i++;
            }
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            connection.rollback(savePoint);
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void updateRecord(String sqlQuery, List<Object> values, Connection connection, long idField) throws SQLException {
        Savepoint savePoint = connection.setSavepoint("savePointName");
        try (PreparedStatement pst = connection.prepareStatement(sqlQuery)) {
            int i = 1;
            for (Object value : values) {
                setValue(pst, i, value);
                i++;
            }
            pst.setLong(i, idField);
            pst.executeUpdate();
        } catch (SQLException ex) {
            connection.rollback(savePoint);
            System.out.println(ex.getMessage());
            throw ex;
        }
    }

    @Override
    public <T> Optional<T> selectRecord(String sqlQuery, long id, Connection connection, Function<ResultSet, T> rsHandler) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(sqlQuery)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        }
    }

    private void setValue(PreparedStatement pst, int i, Object value) throws SQLException {
        Class<?> valueType = value.getClass();
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
    }
}
