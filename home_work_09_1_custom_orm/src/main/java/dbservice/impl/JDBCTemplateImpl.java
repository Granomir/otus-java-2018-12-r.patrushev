package dbservice.impl;

import dbservice.DbExecutor;
import dbservice.JDBCTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test_datasorce.DataSource;
import utils.ReflectionHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class JDBCTemplateImpl implements JDBCTemplate {
    private Logger logger = LoggerFactory.getLogger(JDBCTemplateImpl.class);
    private DbExecutor executor;
    private Map<Class<?>, String> insertQueries;
    private DataSource dataSource;
    private Map<Class<?>, String> updateQueries;
    private Map<Class<?>, String> selectQueries;
    private Map<Class<?>, String> selectCountQueries;

    public JDBCTemplateImpl(DbExecutor executor, DataSource dataSource) {
        this.executor = executor;
        this.dataSource = dataSource;
        insertQueries = new HashMap<>();
        updateQueries = new HashMap<>();
        selectQueries = new HashMap<>();
        selectCountQueries = new HashMap<>();
    }

    @Override
    public <T> long create(T objectData) {
        logger.info("start creating entity");
        Class<?> clazz = objectData.getClass();
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        ReflectionHelper.fillFieldsNamesWithValues(objectData, columns, values);
        long id = -1;
        String sqlQuery = getInsertQuery(clazz, columns);
        logger.info("prepared jdbc template - {}", sqlQuery);
        try (Connection connection = dataSource.getConnection()) {
            id = executor.insertRecord(sqlQuery, values, connection);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("finish creating entity");
        return id;
    }

    private String getInsertQuery(Class<?> clazz, List<String> columns) {
        String query = insertQueries.get(clazz);
        if (null == query) {
            query = String.format("INSERT INTO %s(%s) VALUES (%s)", clazz.getSimpleName(), String.join(",", columns), columns.stream().map(i -> "?").collect(Collectors.joining(",")));
            insertQueries.put(clazz, query);
        }
        return query;
    }

    @Override
    public <T> void update(T objectData) {
        logger.info("start updating entity");
        Class<?> clazz = objectData.getClass();
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        ReflectionHelper.fillFieldsNamesWithValues(objectData, columns, values);
        String idFieldName = ReflectionHelper.getIdFieldName(clazz);
        String sqlQuery = getUpdateQuery(clazz, idFieldName, columns);
        logger.info("prepared jdbc template - {}", sqlQuery);
        try (Connection connection = dataSource.getConnection()) {
            executor.updateRecord(sqlQuery, values, connection, (long) ReflectionHelper.getFieldValue(objectData, idFieldName));
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("finish updating entity");
    }

    private String getUpdateQuery(Class<?> clazz, String idFieldName, List<String> columns) {
        String query = updateQueries.get(clazz);
        if (null == query) {
            query = String.format("UPDATE %s SET %s WHERE %s = ?", clazz.getSimpleName(), columns.stream().map(i -> i + " = ?").collect(Collectors.joining(",")), idFieldName);
            updateQueries.put(clazz, query);
        }
        return query;
    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
        logger.info("start loading entity");
        String sqlQuery = getSelectQuery(clazz, ReflectionHelper.getIdFieldName(clazz));
        logger.info("prepared jdbc template - {}", sqlQuery);
        Optional<T> loadedEntity = Optional.empty();
        try (Connection connection = dataSource.getConnection()) {
            loadedEntity = executor.selectRecord(sqlQuery, id, connection, resultSet -> {
                try {
                    if (resultSet.next()) {
                        T entity = ReflectionHelper.getEmptyEntity(clazz);
                        List<String> fieldsNames = ReflectionHelper.getAllDeclaredFieldsNamesFromClass(clazz);
                        Map<String, Object> values = new HashMap<>();
                        for (String field : fieldsNames) {
                            values.put(field, resultSet.getObject(field));
                        }
                        return ReflectionHelper.fillEntity(clazz, entity, values);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("finish loading entity");
        return loadedEntity.orElseThrow(IllegalArgumentException::new);
    }

    private <T> String getSelectQuery(Class<T> clazz, String idFieldName) {
        String query = selectQueries.get(clazz);
        if (null == query) {
            query = String.format("SELECT * FROM %s WHERE %s = ?", clazz.getSimpleName(), idFieldName);
            selectQueries.put(clazz, query);
        }
        return query;
    }

    private <T> String getSelectCountQuery(Class<T> clazz, String idFieldName) {
        String query = selectCountQueries.get(clazz);
        if (null == query) {
            query = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?", clazz.getSimpleName(), idFieldName);
            selectCountQueries.put(clazz, query);
        }
        return query;
    }

    @Override
    public <T> long createOrUpdate(T objectData) {
        logger.info("start creating or updating entity");
        Class<?> clazz = objectData.getClass();
        String idFieldName = ReflectionHelper.getIdFieldName(clazz);
        long id = (long) ReflectionHelper.getFieldValue(objectData, idFieldName);
        if (id == 0) {
            return create(objectData);
        } else {
            int recordCount = 0;
            String sqlQuery = getSelectCountQuery(clazz, idFieldName);
            logger.info("prepared jdbc template - {}", sqlQuery);
            try (Connection connection = dataSource.getConnection()) {
                recordCount = executor.selectRecordCount(sqlQuery, id, connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (recordCount == 0) {
                return create(objectData);
            } else {
                update(objectData);
                return id;
            }
        }
    }
}
