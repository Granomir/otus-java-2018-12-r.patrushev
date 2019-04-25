package com.patrushev.my_orm.dbutils;

import com.patrushev.my_orm.data.DataSet;
import com.patrushev.my_orm.data.DataSetDAO;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class DBServiceHibernateImpl implements DBService {
    private Logger logger = LoggerFactory.getLogger(DBServiceHibernateImpl.class);

    private DataSetDAO dao;
    private SessionFactory sessionFactory;

    public DBServiceHibernateImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        logger.info("Создан DBService на основе Hibernate с переданной SessionFactory.");
    }

    public DBServiceHibernateImpl(SessionFactory sessionFactory, DataSetDAO dataSetDAO) {
        this.sessionFactory = sessionFactory;
        dao = dataSetDAO;
        logger.info("Создан DBService на основе Hibernate с переданной SessionFactory.");
    }

    @Override
    public <T extends DataSet> void save(T entity) {
        logger.info("Началось сохранение объекта в БД");
        try (Session session = sessionFactory.openSession()) {
            dao.save(session, entity);
        }
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> clazz) {
        logger.info("Началось выгрузка объекта из БД");
        return runInSession(session -> {
            T object = dao.load(session, id, clazz);
            Hibernate.initialize(object);
            logger.info("Объект из БД инициализирован");
            return object;
        });
    }

    private <R> R runInSession(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            logger.info("Открыта транзакция");
            R result = function.apply(session);
            transaction.commit();
            logger.info("Закрыта транзакция");
            return result;
        }
    }
}
