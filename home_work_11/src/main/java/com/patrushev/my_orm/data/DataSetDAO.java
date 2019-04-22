package com.patrushev.my_orm.data;

import com.patrushev.my_orm.dbutils.QueryingHelper;
import com.patrushev.my_orm.executors.Executor;
import com.patrushev.my_orm.utils.ReflectionHelper;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataSetDAO {
    private static Logger logger = LoggerFactory.getLogger(DataSetDAO.class);
    private static QueryingHelper queryingHelper;
    private Session session;

    public DataSetDAO(Session session) {
        this.session = session;
    }

    public DataSetDAO(QueryingHelper queryingHelper) {
        DataSetDAO.queryingHelper = queryingHelper;
    }

    public <T extends DataSet> void save(Connection connection, T entity) throws SQLException {
        logger.info("Сохраняем объект " + entity.toString() + " в БД");
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryingHelper.getInsertQuery(entity))) {
            int count = 1;
            for (Field field : queryingHelper.getFieldList(entity.getClass().getSimpleName())) {
                Class<?> fieldType = field.getType();
                if (!field.getName().equals("id")) {
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
            }
            Executor.updatePrepared(preparedStatement);
        }
    }

    /**
     * возвращает объект переданного класса, собранный из данных, полученных из БД
     */
    private static <T extends DataSet> T getT(Class<T> clazz, ResultSet resultSet) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (resultSet.next()) {
            Constructor<T> constructor = clazz.getConstructor();
            T entity = constructor.newInstance();
            logger.info("Создан объект переданного типа " + clazz.getSimpleName() + " с помощью конструктора по умолчанию");
            for (Field field : queryingHelper.getFieldList(clazz.getSimpleName())) {
                String fieldName = field.getName();
                Object value = resultSet.getObject(fieldName);
                ReflectionHelper.setFieldValue(entity, field, value);
            }
            logger.info("Проинициализированы все поля созданного объекта");
            return entity;
        }
        return null;
    }

    public <T extends DataSet> T load(long id, Connection connection, Class<T> clazz) throws InvocationTargetException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        logger.info("Осуществляется выгрузка из БД объекта типа " + clazz.getSimpleName() + " с id " + id);
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryingHelper.getSelectQuery(id, clazz))) {
            preparedStatement.setLong(1, id);
            return Executor.queryPrepared(preparedStatement, DataSetDAO::getT, clazz);
        }
    }

    public <T extends DataSet> void save(T entity) {
        session.save(entity);
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) {
        return session.load(clazz, id);
    }
}
