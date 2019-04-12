package com.patrushev.my_orm.data;

import com.patrushev.my_orm.dbutils.DBService;
import com.patrushev.my_orm.executors.Executor;
import com.patrushev.my_orm.utils.ReflectionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DataSetDAO {
    private Connection dbConnection;
    private DBService dbService;

    public DataSetDAO(DBService dbService) {
        this.dbConnection = dbService.getCurrentConnection();
        this.dbService = dbService;
    }

    /**
     * Сохраняет переданный объект в БД, если она содержит таблицу, хранящие данные типа переданного объекта
     */
    public <T extends DataSet> void save(T entity) throws SQLException {
        String entityClassName = entity.getClass().getSimpleName();
        if (!dbService.checkTableAvailability(entityClassName))
            throw new IllegalArgumentException("База данных не содержит таблицы для хранения объектов типа " + entityClassName);
        Map<String, Object> fieldsAndValues = ReflectionHelper.getDeclaredFieldsAndValues(entity);
        StringBuilder insertQuery = new StringBuilder();
        insertQuery.append("INSERT INTO ").append(entityClassName).append(" (");
        for (String s : fieldsAndValues.keySet()) {
            insertQuery.append(s).append(", ");
        }
        insertQuery.delete(insertQuery.length() - 2, insertQuery.length());
        insertQuery.append(") VALUES (");
        for (String s : fieldsAndValues.keySet()) {
            if (fieldsAndValues.get(s) instanceof String) {
                insertQuery.append("'").append(fieldsAndValues.get(s)).append("', ");
            } else {
                insertQuery.append(fieldsAndValues.get(s)).append(", ");
            }
        }
        insertQuery.delete(insertQuery.length() - 2, insertQuery.length());
        insertQuery.append(");");
        Executor.update(dbConnection, insertQuery.toString());
    }

    /**
     * возвращает объект, созданный на основе данных полученных из БД по переданному индексу (если БД содержит таблицу, хранящую объекты этого типа)
     */
    public <T extends DataSet> T load(long id, Class<T> clazz) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String className = clazz.getSimpleName();
        if (!dbService.checkTableAvailability(className))
            throw new IllegalArgumentException("База данных не содержит таблицу, хранящую объекты типа " + className);
        return Executor.query(dbConnection, "SELECT * FROM " + className + " WHERE id = " + id + ";", DataSetDAO::getT, clazz);
    }

    /**
     * возвращает объект переданного класса, собранный из данных, полученных из БД
     */
    private static <T extends DataSet> T getT(Class<T> clazz, ResultSet resultSet) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (resultSet.next()) {
            Constructor<T> constructor = clazz.getConstructor();
            //а что делать, если у класса нет конструктора по умолчанию?
            T entity = constructor.newInstance();
            List<Field> fields = ReflectionHelper.getAllFields(entity);
            for (Field field : fields) {
                String fieldName = field.getName();
                Object value = resultSet.getObject(fieldName);
                ReflectionHelper.setFieldValue(entity, field, value);
            }
            return entity;
        }
        return null;
    }
}