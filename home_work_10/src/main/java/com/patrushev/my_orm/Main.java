package com.patrushev.my_orm;

import com.patrushev.my_orm.dbcommon.ConnectionHelper;
import com.patrushev.my_orm.utils.ReflectionHelper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        try (Connection dbConnection = ConnectionHelper.getPostgresqlConnection()) {
//            Executor executor = new Executor(dbConnection);
//            executor.save(new UserDataSet("Roman", 29));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        DataSet dataSet = new UserDataSet("roman", 29);
        System.out.println(dataSet.getClass().getSimpleName());
        List<Field> fields = ReflectionHelper.getAllFields(dataSet);
        for (Field field : fields) {
            System.out.println(ReflectionHelper.getFieldValue(dataSet, field.getName()));
        }
    }
}
