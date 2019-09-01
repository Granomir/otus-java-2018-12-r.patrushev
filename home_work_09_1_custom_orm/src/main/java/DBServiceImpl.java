import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class DBServiceImpl implements DBService {
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
        try (Connection connection = dataSource.getConnection()) {
            id = executor.insertRecord(sqlQuery, values, connection);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try (Connection connection = dataSource.getConnection()) {
            executor.updateRecord(sqlQuery, values, connection, (long) ReflectionHelper.getFieldValue(objectData, idFieldName));
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        List<Field> fields = ReflectionHelper.getAllDeclaredFieldsFromClass(clazz);
        getIdField(clazz, fields);
        String sqlQuery = getSelectQuery(clazz);
        try (Connection connection = dataSource.getConnection()) {
            executor.selectRecord(sqlQuery, id, connection, resultSet -> {
                //TODO здесь инициализация полей рефлексией
                try {
                    if (resultSet.next()) {
                        return new User(resultSet.getLong("id"), resultSet.getString("name"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }););
        } catch (Exception e) {
            e.printStackTrace();
        }

//        List<String> columns = new ArrayList<>();
//        List<Object> values = new ArrayList<>();
//        for (Field field : fields) {
//            String fieldName = field.getName();
//            if (!fieldName.equals(idField.getName())) {
//                columns.add(fieldName);
//                values.add(ReflectionHelper.getFieldValue(objectData, fieldName));
//            }
//        }
//        long id = -1;
//        String sqlQuery = getInsertQuery(clazz, columns);
//        try (Connection connection = dataSource.getConnection()) {
//            id = executor.insertRecord(sqlQuery, columns, values, connection);
//            connection.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return id;
    }

    private <T> String getSelectQuery(Class<T> clazz) {
        String query = selectQueries.get(clazz);
        if (null == query) {
            query = "SELECT FROM " +
                    clazz.getSimpleName() +
                    " WHERE id = ?";
        }
        return query;
    }

    @Override
    public <T> void createOrUpdate(T objectData) {

    }
}
