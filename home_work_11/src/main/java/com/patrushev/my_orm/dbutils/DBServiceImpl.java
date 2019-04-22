package com.patrushev.my_orm.dbutils;

import com.patrushev.my_orm.data.DataSet;
import com.patrushev.my_orm.data.DataSetDAO;
import com.patrushev.my_orm.executors.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DBServiceImpl implements DBService {
    private static Logger logger = LoggerFactory.getLogger(DBServiceImpl.class);
    private Connection connection;
    private Set<Class> usableClasses;
    private QueryingHelper queryingHelper;
    private DataSetDAO dataSetDAO;

    @Override
    public <T extends DataSet> void save(T entity) {
        logger.info("Объект " + entity + " передан на сохранение в БД");
        Class<? extends DataSet> entityClass = entity.getClass();
        if (!usableClasses.contains(entityClass)) {
            logger.info("БД не содержит таблицы, хранящей объекты типа " + entityClass.getSimpleName() + " - сейчас создадим!");
            try {
                Executor.update(connection, queryingHelper.getCreateTableQuery(entityClass));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            logger.info("Таблица для хранения " + entityClass.getSimpleName() + " создана");
            usableClasses.add(entityClass);
        }
        try {
            dataSetDAO.save(connection, entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> clazz) {
        logger.info("Объект типа" + clazz.getSimpleName() + " под id " + id + " запрошен из БД");
        if (usableClasses.contains(clazz)) {
            try {
                return dataSetDAO.load(id, connection, clazz);
            } catch (InvocationTargetException | SQLException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalArgumentException("БД не содержит таблицы, хранящей объекты типа " + clazz.getSimpleName());
    }

    public DBServiceImpl(Connection connection, QueryingHelper queryingHelper, DataSetDAO dataSetDAO) {
        this.connection = connection;
        this.dataSetDAO = dataSetDAO;
        usableClasses = new HashSet<>();
        this.queryingHelper = queryingHelper;
    }
}
