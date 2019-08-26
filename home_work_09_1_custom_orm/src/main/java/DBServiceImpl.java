import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;

public class DBServiceImpl implements DBService {
    private DbExecutor executor;
    private Map<Class<?>, Field> acceptableClasses;
    private Map<Class<?>, String> insertQueries;
    private DataSource dataSource;
    private Map<Class<?>, String> updateQueries;

    public DBServiceImpl(DbExecutor executor, DataSource dataSource) {
        this.executor = executor;
        this.dataSource = dataSource;
        acceptableClasses = new HashMap<>();
        insertQueries = new HashMap<>();
        updateQueries = new HashMap<>();
    }

    @Override
    public <T> int create(T objectData) {
        Class<?> clazz = objectData.getClass();
        List<Field> fields = ReflectionHelper.getAllDeclaredFieldsFromClass(clazz);
        Field idField = acceptableClasses.get(clazz);
        if (idField == null) {
            idField = getIdField(fields);
            acceptableClasses.put(clazz, idField);
        }
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!fieldName.equals(idField.getName())) {
                columns.add(fieldName);
                values.add(ReflectionHelper.getFieldValue(objectData, fieldName));
            }
        }
        int id = -1;
        String sqlQuery = getInsertQuery(clazz, columns);
        try (Connection connection = dataSource.getConnection()) {
            id = executor.insertRecord(sqlQuery, columns, values, connection);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    private String getInsertQuery(Class<?> clazz, List<String> columns) {
        String query = insertQueries.get(clazz);
        if (null != query) {
            return query;
        } else {
            StringBuilder sqlQuery = new StringBuilder("INSERT INTO ")
                    .append(clazz.getSimpleName())
                    .append("(");
            String filler = columns.stream().map(i -> "?").collect(Collectors.joining(","));
            sqlQuery.append(filler)
                    .append(") VALUES (")
                    .append(filler)
                    .append(")");
            query = sqlQuery.toString();
            insertQueries.put(clazz, query);
            return query;
        }
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
        Field idField = acceptableClasses.get(clazz);
        if (idField == null) {
            idField = getIdField(fields);
            acceptableClasses.put(clazz, idField);
        }
        Map<String, Object> columnsAndValues = new HashMap<>();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!fieldName.equals(idField.getName())) {
                columnsAndValues.put(fieldName, ReflectionHelper.getFieldValue(objectData, fieldName));
            }
        }
        String sqlQuery = getUpdateQuery(clazz, columnsAndValues, idField);
        try (Connection connection = dataSource.getConnection()) {
            executor.updateRecord(sqlQuery, columnsAndValues, connection);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUpdateQuery(Class<?> clazz, Map<String, Object> columnsAndValues, Field idField) {
        String query = updateQueries.get(clazz);
        if (null != query) {
            return query;
        } else {
            StringBuilder sqlQuery = new StringBuilder("UPDATE ")
                    .append(clazz.getSimpleName())
                    .append(" SET ");
            String filler = columnsAndValues.keySet().stream().map(i -> "? = ?").collect(Collectors.joining(","));
            sqlQuery.append(filler)
                    .append(" WHERE id = ?");
            query = sqlQuery.toString();
            insertQueries.put(clazz, query);
            return query;
        }
    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> void createOrUpdate(T objectData) {

    }
}
