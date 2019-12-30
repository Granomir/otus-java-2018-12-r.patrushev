package dbservice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface DbExecutor {

    int insertRecord(String sqlQuery, List<Object> values) throws SQLException;

    void updateRecord(String sqlQuery, List<Object> values, long fieldValue) throws SQLException;

    <T> Optional<T> selectRecord(String sqlQuery, long id, Function<ResultSet, T> rsHandler) throws SQLException;

    int selectRecordCount(String sqlQuery, long id) throws SQLException;
}
