import java.sql.Connection;
import java.util.List;

public class DbExecutorImpl implements DbExecutor {

    @Override
    public int insertRecord(String sqlQuery, List<String> columns, List<Object> values, Connection connection) {
        return 0;
    }
}
