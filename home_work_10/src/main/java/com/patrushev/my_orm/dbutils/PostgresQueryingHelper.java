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
    private Logger logger = LoggerFactory.getLogger(PostgresQueryingHelper.class);
    private Map<String, String> insertQueries;
    private Map<String, List<Field>> fieldsLists;

    @Override
    public String getCreateTableQuery(Class<? extends DataSet> entityClass) {
        //создаю запрос CREATE
        StringBuilder createQuery = new StringBuilder();
        String classSimpleName = entityClass.getSimpleName();
        createQuery.append("CREATE TABLE IF NOT EXISTS ").append(classSimpleName).append(" (id BIGSERIAL NOT NULL PRIMARY KEY, ");
        //получаю поля класса
        List<Field> fields = ReflectionHelper.getAllDeclaredFieldsFromClass(entityClass);
        //добавляю поля класса и их тип к запросу
        for (Field field : fields) {
            createQuery.append(field.getName()).append(" ");
            Class<?> fieldType = field.getType();
            if (fieldType.equals(Long.TYPE)) createQuery.append("BIGINTEGER, ");
            if (fieldType.equals(Integer.TYPE)) createQuery.append("INTEGER, ");
            if (fieldType.equals(Short.TYPE) || fieldType.equals(Byte.TYPE))
                createQuery.append("SMALLINT, ");
            if (fieldType.equals(Double.TYPE)) createQuery.append("DOUBLE PRECISION, ");
            if (fieldType.equals(Float.TYPE)) createQuery.append("REAL, ");
            if (fieldType.equals(Character.TYPE) || fieldType.equals(String.class))
                createQuery.append("VARCHAR(255), ");
        }
        createQuery.delete(createQuery.length() - 2, createQuery.length());
        createQuery.append(");");
        logger.info("Подготовлен запрос на создание новой таблицы - " + createQuery);
        //сохраняю поля класса
        fieldsLists.put(classSimpleName, fields);
        //добавляю запросы insert и select в Map
        prepareInsertQuery(entityClass);
        //возвращаю окончательный запрос
        return createQuery.toString();
    }

    private void prepareInsertQuery(Class<? extends DataSet> entityClass) {
        StringBuilder insertQuery = new StringBuilder();
        String classSimpleName = entityClass.getSimpleName();
        insertQuery.append("INSERT INTO ").append(classSimpleName).append(" (");
        //добавляю поля класса и их тип к запросу
        List<Field> fieldList = fieldsLists.get(classSimpleName);
        for (Field field : fieldList) {
            insertQuery.append(field.getName()).append(", ");
        }
        insertQuery.delete(insertQuery.length() - 2, insertQuery.length());
        insertQuery.append(") VALUES (");
        insertQuery.append("?, ".repeat(fieldList.size()));
        insertQuery.delete(insertQuery.length() - 2, insertQuery.length());
        insertQuery.append(");");
        insertQueries.put(classSimpleName, insertQuery.toString());
        logger.info("Подготовлен и сохранен INSERT запрос для таблицы " + classSimpleName + " - " + insertQuery);
    }

    @Override
    public <T> String getInsertQuery(T entity) {
        String query = insertQueries.get(entity.getClass().getSimpleName());
        logger.info("Получен из кэша INSERT запрос для таблицы " + entity.getClass().getSimpleName() + " - " + query);
        return query;
    }

    @Override
    public <T> String getSelectQuery(T entity) {
        String query = "SELECT * FROM " + entity.getClass().getSimpleName() + ";";
        logger.info("Подготовлен SELECT запрос для таблицы " + entity.getClass().getSimpleName() + " - " + query);
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
