package com.patrushev.my_orm.dbcommon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {

    public static Connection getPostgresqlConnection() throws SQLException {
        final String connString = "jdbc:postgresql://" +    // db type
                "localhost:" +                              // host name
                "5432/" +                                   // port
                "mydb?" +                                   // db name
                "user=postgres";                            // login
        return DriverManager.getConnection(connString);
    }
}
