package com.patrushev.my_orm.dbutils;

import com.patrushev.my_orm.data.DataSet;
import com.patrushev.my_orm.utils.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostgresQueryingHelper implements QueryingHelper {
    private final Logger logger = LoggerFactory.getLogger(PostgresQueryingHelper.class);
    private final Map<String, String> insertQueries;
    private final Map<String, List<Field>> fieldsLists;

    @Override
    public String getCreateTableQuery(Class<? extends DataSet> entityClass) {
        StringBuilder createQuery = new StringBuilder();
        String classSimpleName = entityClass.getSimpleName();
        createQuery.append("CREATE TABLE IF NOT EXISTS ").append(classSimpleName).append(" (");
        List<Field> fields = ReflectionHelper.getAllFieldsFromClass(entityClass);
        for (Field field : fields) {
            createQuery.append(field.getName()).append(" ");
            Class<?> fieldType = field.getType();
            if (field.getName().equals("id")) {
                createQuery.append("BIGSERIAL NOT NULL PRIMARY KEY, ");
            } else {
                if (fieldType.equals(Long.TYPE)) createQuery.append("BIGINT, ");
                if (fieldType.equals(Integer.TYPE)) createQuery.append("INTEGER, ");
                if (fieldType.equals(Short.TYPE) || fieldType.equals(Byte.TYPE))
                    createQuery.append("SMALLINT, ");
                if (fieldType.equals(Double.TYPE)) createQuery.append("DOUBLE PRECISION, ");
                if (fieldType.equals(Float.TYPE)) createQuery.append("REAL, ");
                if (fieldType.equals(Character.TYPE) || fieldType.equals(String.class))
                    createQuery.append("VARCHAR(255), ");
            }
        }
        createQuery.delete(createQuery.length() - 2, createQuery.length());
        createQuery.append(");");
        logger.info("Подготовлен запрос на создание новой таблицы - " + createQuery);
        fieldsLists.put(classSimpleName, fields);
        prepareInsertQuery(entityClass);
        return createQuery.toString();
    }

    private void prepareInsertQuery(Class<? extends DataSet> entityClass) {
        StringBuilder insertQuery = new StringBuilder();
        String classSimpleName = entityClass.getSimpleName();
        insertQuery.append("INSERT INTO ").append(classSimpleName).append(" (");
        List<Field> fieldList = fieldsLists.get(classSimpleName);
        for (Field field : fieldList) {
            if (!field.getName().equals("id")) {
                insertQuery.append(field.getName()).append(", ");
            }
        }
        insertQuery.delete(insertQuery.length() - 2, insertQuery.length());
        insertQuery.append(") VALUES (");
        for (Field field : fieldList) {
            if (!field.getName().equals("id")) {
                insertQuery.append("?, ");
            }
        }
        insertQuery.delete(insertQuery.length() - 2, insertQuery.length());
        insertQuery.append(");");
        insertQueries.put(classSimpleName, insertQuery.toString());
        logger.info("Подготовлен и сохранен INSERT запрос для таблицы " + classSimpleName + " - " + insertQuery);
    }

    @Override
    public <T> String getInsertQuery(T entity) {
        String className = entity.getClass().getSimpleName();
        String query = insertQueries.get(className);
        logger.info("Получен из кэша INSERT запрос для таблицы " + className + " - " + query);
        return query;
    }

    @Override
    public <T> String getSelectQuery(long id, Class<T> clazz) {
        String clazzSimpleName = clazz.getSimpleName();
        String query = "SELECT * FROM " + clazzSimpleName + " WHERE id = ?;";
        logger.info("Подготовлен SELECT запрос для таблицы " + clazzSimpleName + " - " + query);
        return query;
    }

    public PostgresQueryingHelper() {
        fieldsLists = new HashMap<>();
        insertQueries = new HashMap<>();
    }

    public List<Field> getFieldList(String className) {
        return fieldsLists.get(className);
    }
}
