package dbservice.impl;

import dbservice.DbExecutor;
import dbservice.JDBCTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ReflectionHelper;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class JDBCTemplateImpl implements JDBCTemplate {
    private final Logger logger = LoggerFactory.getLogger(JDBCTemplateImpl.class);
    private final DbExecutor executor;
    private final Map<Class<?>, String> insertQueries;
    private final Map<Class<?>, String> updateQueries;
    private final Map<Class<?>, String> selectQueries;
    private final Map<Class<?>, String> selectCountQueries;

    public JDBCTemplateImpl(DbExecutor executor) {
        this.executor = executor;
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
        try {
            id = executor.insertRecord(sqlQuery, values);
        } catch (SQLException e) {
            logger.error("Error occurred while creating entity: {}", objectData, e);
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
        try {
            executor.updateRecord(sqlQuery, values, (long) ReflectionHelper.getFieldValue(objectData, idFieldName));
        } catch (SQLException e) {
            logger.error("Error occurred while updating entity: {}", objectData, e);
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
        try {
            loadedEntity = executor.selectRecord(sqlQuery, id, resultSet -> {
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
                    logger.error("Error occurred while deserialize entity with id: {}", id, e);
                }
                return null;
            });
        } catch (SQLException e) {
            logger.error("Error occurred while loading entity with id: {}", id, e);
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
            try {
                recordCount = executor.selectRecordCount(sqlQuery, id);
            } catch (SQLException e) {
                logger.error("Error occurred while creatingOrUpdating entity: {}", objectData, e);
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
