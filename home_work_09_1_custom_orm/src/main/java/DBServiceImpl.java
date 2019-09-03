import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class DBServiceImpl implements DBService {
    private Logger logger = LoggerFactory.getLogger(DBServiceImpl.class);
    private DbExecutor executor;
    private Map<Class<?>, Field> acceptableClasses;
    private Map<Class<?>, String> insertQueries;
    private DataSource dataSource;
    private Map<Class<?>, String> updateQueries;
    private Map<Class<?>, String> selectQueries;

    public DBServiceImpl(DbExecutor executor, DataSource dataSource) {
        this.executor = executor;
        this.dataSource = dataSource;
        acceptableClasses = new HashMap<>();
        insertQueries = new HashMap<>();
        updateQueries = new HashMap<>();
        selectQueries = new HashMap<>();
    }

    @Override
    public <T> long create(T objectData) {
        logger.info("start creating entity");
        Class<?> clazz = objectData.getClass();
        List<Field> fields = ReflectionHelper.getAllDeclaredFieldsFromClass(clazz);
        Field idField = getIdField(clazz, fields);
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!fieldName.equals(idField.getName())) {
                columns.add(fieldName);
                values.add(ReflectionHelper.getFieldValue(objectData, fieldName));
            }
        }
        long id = -1;
        String sqlQuery = getInsertQuery(clazz, columns);
        logger.info("prepared jdbc template - " + sqlQuery);
        try (Connection connection = dataSource.getConnection()) {
            id = executor.insertRecord(sqlQuery, values, connection);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("finish creating entity");
        return id;
    }

    private Field getIdField(Class<?> clazz, List<Field> fields) {
        Field idField = acceptableClasses.get(clazz);
        if (idField == null) {
            idField = getIdField(fields);
            acceptableClasses.put(clazz, idField);
        }
        return idField;
    }

    private String getInsertQuery(Class<?> clazz, List<String> columns) {
        String query = insertQueries.get(clazz);
        if (null == query) {
            query = "INSERT INTO " +
                    clazz.getSimpleName() +
                    "(" +
                    String.join(",", columns) +
                    ") VALUES (" +
                    columns.stream().map(i -> "?").collect(Collectors.joining(",")) +
                    ")";
            insertQueries.put(clazz, query);
        }
        return query;
    }

    private Field getIdField(List<Field> fields) {
        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null) {
                return field;
            }
        }
        throw new IllegalArgumentException("DBService может работать только с классами, имеющими поле с аннотацией \"@Id\"");
    }

    @Override
    public <T> void update(T objectData) {
        logger.info("start updating entity");
        Class<?> clazz = objectData.getClass();
        List<Field> fields = ReflectionHelper.getAllDeclaredFieldsFromClass(clazz);
        Field idField = getIdField(clazz, fields);
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        String idFieldName = idField.getName();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!fieldName.equals(idFieldName)) {
                columns.add(fieldName);
                values.add(ReflectionHelper.getFieldValue(objectData, fieldName));
            }
        }
        String sqlQuery = getUpdateQuery(clazz, columns);
        logger.info("prepared jdbc template - " + sqlQuery);
        try (Connection connection = dataSource.getConnection()) {
            executor.updateRecord(sqlQuery, values, connection, (long) ReflectionHelper.getFieldValue(objectData, idFieldName));
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("finish updating entity");
    }

    private String getUpdateQuery(Class<?> clazz, List<String> columns) {
        String query = updateQueries.get(clazz);
        if (null == query) {
            query = "UPDATE " +
                    clazz.getSimpleName() +
                    " SET " +
                    columns.stream().map(i -> i + " = ?").collect(Collectors.joining(",")) +
                    " WHERE id = ?";
            updateQueries.put(clazz, query);
        }
        return query;
    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
        logger.info("start loading entity");
        List<Field> fields = ReflectionHelper.getAllDeclaredFieldsFromClass(clazz);
        getIdField(clazz, fields);
        String sqlQuery = getSelectQuery(clazz);
        logger.info("prepared jdbc template - " + sqlQuery);
        Optional<T> loadedEntity = Optional.empty();
        try (Connection connection = dataSource.getConnection()) {
            loadedEntity = executor.selectRecord(sqlQuery, id, connection, resultSet -> {
                try {
                    if (resultSet.next()) {
                        Constructor<T> constructor = clazz.getConstructor();
                        T entity = constructor.newInstance();
                        for (Field field : fields) {
                            ReflectionHelper.setFieldValue(entity, field, resultSet.getObject(field.getName()));
                        }
                        return entity;
                    }
                } catch (SQLException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
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

    private <T> String getSelectQuery(Class<T> clazz) {
        String query = selectQueries.get(clazz);
        if (null == query) {
            query = "SELECT FROM " +
                    clazz.getSimpleName() +
                    " WHERE id = ?";
            selectQueries.put(clazz, query);
        }
        return query;
    }

    @Override
    public <T> void createOrUpdate(T objectData) {

    }
}
