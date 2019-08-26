import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;

public class DBServiceImpl implements DBService {
    private DbExecutor executor;
    private List<Class<?>> acceptableClasses;
    private Map<Class<?>, String> insertQueries;
    private DataSource dataSource;

    public DBServiceImpl(DbExecutor executor, DataSource dataSource) {
        this.executor = executor;
        this.dataSource = dataSource;
        acceptableClasses = new ArrayList<>();
        insertQueries = new HashMap<>();
    }

    @Override
    public <T> void create(T objectData) {
        Class<?> clazz = objectData.getClass();
        List<Field> fields = ReflectionHelper.getAllDeclaredFieldsFromClass(clazz);
        if (acceptableClasses.contains(clazz)) {
            checkIdField(fields);
            acceptableClasses.add(clazz);
        }
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        for (Field field : fields) {
            String fieldName = field.getName();
            columns.add(fieldName);
            values.add(ReflectionHelper.getFieldValue(objectData, fieldName));
        }
        String sqlQuery = getInsertQuery(clazz, columns);
        try (Connection connection = dataSource.getConnection()){
            //TODO нужно ли тут возвращать id???
            executor.insertRecord(sqlQuery, columns, values, connection);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void checkIdField(List<Field> fields) {
        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null) {
                return;
            }
        }
        throw new IllegalArgumentException("DBService может работать только с классами, имеющими поле с аннотацией \"@Id\"");
    }

    @Override
    public <T> void update(T objectData) {

    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> void createOrUpdate(T objectData) {

    }
}
