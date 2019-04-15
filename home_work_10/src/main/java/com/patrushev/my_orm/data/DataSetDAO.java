package com.patrushev.my_orm.data;

import com.patrushev.my_orm.dbutils.DBService;
import com.patrushev.my_orm.dbutils.DMLHelper;
import com.patrushev.my_orm.dbutils.QueryingHelper;
import com.patrushev.my_orm.executors.Executor;
import com.patrushev.my_orm.utils.ReflectionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DataSetDAO {
    private DMLHelper dmlHelper;
    private static Logger logger = LoggerFactory.getLogger(DataSetDAO.class);
    private Connection dbConnection;
    private DBService dbService;
    private Map<String, List<String>> usableClasses;

    public DataSetDAO() {
    }

    public <T extends DataSet> void save(Connection connection, QueryingHelper queryingHelper, T entity) throws SQLException {
        logger.info("Сохраняем объект " + entity.toString() + " в БД");
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryingHelper.getInsertQuery(entity))) {
            int count = 1;
            for (Field field : queryingHelper.getFieldList(entity.getClass().getSimpleName())) {
                Class<?> fieldType = field.getType();
                if (fieldType.equals(Long.TYPE))
                    preparedStatement.setLong(count, (long) ReflectionHelper.getFieldValue(entity, field.getName()));
                if (fieldType.equals(Integer.TYPE))
                    preparedStatement.setInt(count, (int) ReflectionHelper.getFieldValue(entity, field.getName()));
                if (fieldType.equals(Short.TYPE) || fieldType.equals(Byte.TYPE))
                    preparedStatement.setShort(count, (short) ReflectionHelper.getFieldValue(entity, field.getName()));
                if (fieldType.equals(Double.TYPE))
                    preparedStatement.setDouble(count, (double) ReflectionHelper.getFieldValue(entity, field.getName()));
                if (fieldType.equals(Float.TYPE))
                    preparedStatement.setFloat(count, (float) ReflectionHelper.getFieldValue(entity, field.getName()));
                if (fieldType.equals(Character.TYPE) || fieldType.equals(String.class))
                    preparedStatement.setString(count, (String) ReflectionHelper.getFieldValue(entity, field.getName()));
                count++;
            }
            Executor.updatePrepared(connection, preparedStatement);
        }
    }

//    /**
//     * Сохраняет переданный объект в БД, если она содержит таблицу, хранящие данные типа переданного объекта
//     */
//    public <T extends DataSet> void save(T entity) throws SQLException {
//
//        String entityClassName = entity.getClass().getSimpleName();
//        logger.info("Осуществляется попытка записи в БД объекта типа " + entityClassName);
//        logger.info("Проверяется наличие таблицы в БД, которая может содержать объекты переданного типа");
//        if (!dbService.checkTableAvailability(entityClassName))
//            throw new IllegalArgumentException("База данных не содержит таблицы для хранения объектов типа " + entityClassName);
//        Map<String, Object> fieldsAndValues = ReflectionHelper.getDeclaredFieldsAndValues(entity);
//        logger.info("Получены поля и их значения переданного объекта");
//        StringBuilder insertQuery = new StringBuilder();
//        insertQuery.append("INSERT INTO ").append(entityClassName).append(" (");
//        for (String s : fieldsAndValues.keySet()) {
//            insertQuery.append(s).append(", ");
//        }
//        insertQuery.delete(insertQuery.length() - 2, insertQuery.length());
//        insertQuery.append(") VALUES (");
//        for (String s : fieldsAndValues.keySet()) {
//            if (fieldsAndValues.get(s) instanceof String) {
//                insertQuery.append("'").append(fieldsAndValues.get(s)).append("', ");
//            } else {
//                insertQuery.append(fieldsAndValues.get(s)).append(", ");
//            }
//        }
//        insertQuery.delete(insertQuery.length() - 2, insertQuery.length());
//        insertQuery.append(");");
//        Executor.update(dbConnection, insertQuery.toString());
//    }

    /**
     * возвращает объект, созданный на основе данных полученных из БД по переданному индексу (если БД содержит таблицу, хранящую объекты этого типа)
     */
    public <T extends DataSet> T load(long id, Class<T> clazz) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String className = clazz.getSimpleName();
        logger.info("Осуществляется попытка выгрузки из БД объекта типа " + className + " с id " + id);
        logger.info("Проверяется наличие таблицы в БД, которая может содержать объекты переданного типа");
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
            logger.info("Создан объект переданного типа с помощью конструктора по умолчанию");
            List<Field> fields = ReflectionHelper.getAllFields(entity);
            for (Field field : fields) {
                String fieldName = field.getName();
                Object value = resultSet.getObject(fieldName);
                ReflectionHelper.setFieldValue(entity, field, value);
            }
            logger.info("Проинициализированы все поля созданного объекта");
            return entity;
        }
        return null;
    }
}
