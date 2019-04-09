package com.patrushev.my_orm;

import com.patrushev.my_orm.utils.ReflectionHelper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSetDAO {
    private Connection dbConnection;
    private DBService dbService;

    public DataSetDAO(DBService dbService) {
        this.dbConnection = dbService.getCurrentConnection();
        this.dbService = dbService;
    }

    <T extends DataSet> void save(T entity) throws SQLException {
        //проверяем есть ли таблица в БД для хранения данных переданного типа, если нет - создаем
        String entityName = entity.getClass ().getSimpleName();
        if (!dbService.checkTableAvailability(entityName)) dbService.createTable(entityName);
        //разбираем entity на поля+значения
        Map<String, Object> fieldsAndValues = getFieldsAndValues(entity);
        //создаем запрос на основе полученных полей
        StringBuilder update = new StringBuilder();
        update.append("INSERT INTO ").append(entityName).append(" (");
        for (String s : fieldsAndValues.keySet()) {
            update.append(s).append(", ");
        }
        update.delete(update.length() - 2, update.length());
        //здесь продолжить составлять запрос на INSERT
    }

    private <T extends DataSet> Map<String, Object> getFieldsAndValues(T user) {
        Map<String, Object> fieldsAndValues = new HashMap<>();
        List<Field> fields = ReflectionHelper.getAllFields(user);
        for (Field field : fields) {
            fieldsAndValues.put(field.getName(), ReflectionHelper.getFieldValue(user, field.getName()));
        }
        return fieldsAndValues;
    }

    <T extends DataSet> T load(long id, Class<T> clazz) {


        return null;
    }
}
