package com.patrushev.my_orm;

import com.patrushev.my_orm.utils.ReflectionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DataSetDAO {
    private Connection dbConnection;
    private DBService dbService;

    public DataSetDAO(DBService dbService) {
        this.dbConnection = dbService.getCurrentConnection();
        this.dbService = dbService;
    }

    /**
     * сохраняет переданный объект в БД, определяя по его типу в какую таблицу его следует сохранить
     * @param entity - объект
     * @param <T> - тип объекта
     * @throws SQLException
     */
    <T extends DataSet> void save(T entity) throws SQLException {
        //проверяем есть ли таблица в БД для хранения данных переданного типа, если нет - кидаем исключение
        String entityClassName = entity.getClass ().getSimpleName();
        if (!dbService.checkTableAvailability(entityClassName)) throw new IllegalArgumentException("База данных не содержит таблицы для хранения объектов типа " + entityClassName);
        //разбираем entity на поля+значения
        Map<String, Object> fieldsAndValues = ReflectionHelper.getDeclaredFieldsAndValues(entity);
        //создаем INSERT-запрос на основе полученных полей
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
        //передаем запрос экзекутору
        System.out.println(insertQuery);
        Executor.update(dbConnection, insertQuery.toString());
    }

    <T extends DataSet> T load(long id, Class<T> clazz) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //проверяем есть ли таблица в БД для хранения данных переданного типа, если нет - кидаем исключение
        String className = clazz.getSimpleName();
        if (!dbService.checkTableAvailability(className)) throw new IllegalArgumentException("База данных не содержит таблицу, хранящую объекты типа " + className);
        //считать строку с базы по id
        ResultSet resultSet = Executor.query(dbConnection, "SELECT * FROM " + className.toLowerCase() + " WHERE id = " + id + ";");
        //создать объект на основе полученных данных
        if (resultSet.next()) {
            Constructor<T> constructor = clazz.getConstructor();
            T entity = constructor.newInstance();
            //получить все поля класса (включая унаследованные)
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                //получаю имя поля
                String fieldName = field.getName();
                //получаю значение для записи в поле из БД
                Object value = resultSet.getObject(fieldName);
                field.set(entity, value);
            }
            return entity;
            //получить по названиям этих полей данные из resultSet

            //создать объект

            //проинициализировать поля

        }
        return null;
    }
}
