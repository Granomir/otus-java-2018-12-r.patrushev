package test_datasorce;

import java.sql.Connection;

public interface DataSource {
    Connection getConnection();
}
