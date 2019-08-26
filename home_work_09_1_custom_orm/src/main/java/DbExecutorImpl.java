import java.sql.*;
import java.util.List;

public class DbExecutorImpl implements DbExecutor {

    @Override
    public int insertRecord(String sqlQuery, List<String> columns, List<Object> values, Connection connection) throws SQLException {
        Savepoint savePoint = connection.setSavepoint("savePointName");
        try (PreparedStatement pst = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            for (String column : columns) {
                pst.setString(i, column);
                i++;
            }
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
