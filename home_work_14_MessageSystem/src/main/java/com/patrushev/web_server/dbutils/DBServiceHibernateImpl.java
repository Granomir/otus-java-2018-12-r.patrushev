package com.patrushev.web_server.dbutils;

import com.patrushev.web_server.data.DataSet;
import com.patrushev.web_server.data.UserDataSet;
import com.patrushev.web_server.data.UserDataSetDAO;
import com.patrushev.web_server.messageSystem.Address;
import com.patrushev.web_server.messageSystem.MessageSystem;
import com.patrushev.web_server.messageSystem.MessageSystemContext;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;

public class DBServiceHibernateImpl implements DBService {
    private final Logger logger = LoggerFactory.getLogger(DBServiceHibernateImpl.class);

    private final UserDataSetDAO dao;
    private final MessageSystemContext msContext;
    private final Address address;
    private final SessionFactory sessionFactory;

    public DBServiceHibernateImpl(Configuration configuration, UserDataSetDAO userDataSetDAO, MessageSystemContext msContext, Address address) {
        this.sessionFactory = configuration.buildSessionFactory();
        this.dao = userDataSetDAO;
        this.msContext = msContext;
        this.address = address;
        logger.info("Создан DBService на основе Hibernate с переданной SessionFactory.");
        msContext.getMessageSystem().addAddressee(this);
    }

    @Override
    public <T extends DataSet> void save(T entity) {
        logger.info("Началось сохранение объекта в БД");
        runInSession(session -> {
            dao.save(session, entity);
            return null;
        });
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> clazz) {
        logger.info("Началась выгрузка объекта из БД");
        return runInSession(session -> {
            T object = dao.load(session, id, clazz);
            Hibernate.initialize(object);
            logger.info("Объект из БД инициализирован");
            return object;
        });
    }

    @Override
    public UserDataSet loadUserByName(String name) {
        logger.info("Началась выгрузка пользователя из БД по имени");
        return runInSession(session -> {
            UserDataSet user = dao.readByName(session, name);
            Hibernate.initialize(user);
            logger.info("Пользователь из БД инициализирован");
            return user;
        });
    }

    @Override
    public List<UserDataSet> getAllUsers() {
        logger.info("Началась выгрузка всех пользователей из БД");
        return runInSession(dao::readAll);
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

    @Override
    public void close() {
        sessionFactory.close();
        System.out.println("Нету больше sessionFactory! :(");
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMS() {
        return msContext.getMessageSystem();
    }
}
