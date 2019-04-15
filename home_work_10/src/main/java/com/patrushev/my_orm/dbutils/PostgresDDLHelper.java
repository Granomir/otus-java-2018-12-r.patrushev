package com.patrushev.my_orm.dbutils;

import com.patrushev.my_orm.data.DataSet;
import com.patrushev.my_orm.utils.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;

public class PostgresDDLHelper implements DDLHelper {

    @Override
    public String getCreateTableQuery(Class<? extends DataSet> entityClass) {
        //создаю запрос CREATE
        StringBuilder createQuery = new StringBuilder();
        createQuery.append("CREATE TABLE IF NOT EXISTS ").append(entityClass.getSimpleName()).append(" (id BIGSERIAL NOT NULL PRIMARY KEY, ");
        //получаю поля класса
        List<Field> fields = ReflectionHelper.getAllDeclaredFieldsFromClass(entityClass);
        //добавляю поля класса и их тип к запросу
        for (Field field : fields) {
            createQuery.append(field.getName()).append(" ");
            if (field.getType().equals(Long.TYPE)) createQuery.append("BIGINTEGER, ");
            if (field.getType().equals(Integer.TYPE)) createQuery.append("INTEGER, ");
            if (field.getType().equals(Short.TYPE) || field.getType().equals(Byte.TYPE))
                createQuery.append("SMALLINT, ");
            if (field.getType().equals(Double.TYPE)) createQuery.append("DOUBLE PRECISION, ");
            if (field.getType().equals(Float.TYPE)) createQuery.append("REAL, ");
            if (field.getType().equals(Character.TYPE) || field.getType().equals(String.class))
                createQuery.append("VARCHAR(255), ");
        }
        createQuery.delete(createQuery.length() - 2, createQuery.length());
        createQuery.append(");");
        //возвращаю окончательный запрос
        return createQuery.toString();
    }
}
