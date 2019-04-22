package com.patrushev.my_orm.dbutils;

import com.patrushev.my_orm.data.DataSet;
import com.patrushev.my_orm.data.DataSetDAO;
import com.patrushev.my_orm.utils.HibernateSessionFactory;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.function.Function;

public class DBServiceHibernateImpl implements DBService {
    private SessionFactory sessionFactory;

    public DBServiceHibernateImpl(Configuration configuration) {
//        sessionFactory = createSessionFactory(configuration);
        sessionFactory = HibernateSessionFactory.getSessionFactory();
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        //этот код создает sessionFactory
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    public <T extends DataSet> void save(T entity) {
        //создаю сессию
        try (Session session = sessionFactory.openSession()) {
            //создаю DAO с передачей ему сессии
            DataSetDAO dao = new DataSetDAO(session);
            //сохраняем переданный объект через DAO
            dao.save(entity);
        }
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> clazz) {
        return runInSession((Session session) -> {
            DataSetDAO dao = new DataSetDAO(session);
            T object = dao.load(id, clazz);
            Hibernate.initialize(object);
            return object;
        });
    }

    private <R> R runInSession(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }
}
