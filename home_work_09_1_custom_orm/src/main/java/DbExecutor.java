import java.sql.Connection;
import java.util.List;

public interface DbExecutor {

    void insertRecord(String sqlQuery, List<String> columns, List<Object> values, Connection connection);
}
