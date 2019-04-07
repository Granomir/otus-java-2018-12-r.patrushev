package com.patrushev.my_orm;

import com.patrushev.my_orm.utils.ReflectionHelper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSetDAO {
    private Connection dbConnection;

    public DataSetDAO(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    <T extends DataSet> void save(T entity) {
        //проверяем есть ли таблица в БД для хранения данных переданного типа

        //разбираем entity на поля+значения
        Map<String, Object> fieldsAndValues = getFieldsAndValues(entity);
        //создаем запрос на основе полученных полей
        StringBuilder update = new StringBuilder();
        update.append("INSERT INTO ").append(entity.getClass ().getSimpleName()).append(" (");
        for (String s : fieldsAndValues.keySet()) {
            update.append(s).append(", ");
        }
        update.delete(update.length() - 2, update.length());
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
