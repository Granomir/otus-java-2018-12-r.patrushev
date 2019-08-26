import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DbExecutor {

    int insertRecord(String sqlQuery, List<String> columns, List<Object> values, Connection connection) throws SQLException;

    void updateRecord(String sqlQuery, Map<String, Object> columnsAndValues, Connection connection, long fieldValue) throws SQLException;
}
