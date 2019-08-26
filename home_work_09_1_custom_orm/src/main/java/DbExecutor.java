import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DbExecutor {

    int insertRecord(String sqlQuery, List<String> columns, List<Object> values, Connection connection) throws SQLException;
}
