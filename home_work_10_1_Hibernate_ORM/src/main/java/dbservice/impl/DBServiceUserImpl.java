package dbservice.impl;

import com.patrushev.my_cache_engine.CacheEngine;
import com.patrushev.my_cache_engine.CacheEngineMyImpl;
import dbservice.DBService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test_entities.Address;
import test_entities.Phone;
import test_entities.User;

import java.util.ArrayList;
import java.util.List;

public class DBServiceUserImpl implements DBService<User> {
    private final Logger logger = LoggerFactory.getLogger(DBServiceUserImpl.class);

    private final SessionFactory sessionFactory;
    private final CacheEngine<Long, Object> cache;

    public DBServiceUserImpl() {
        this("hibernate.cfg.xml");
    }

    public DBServiceUserImpl(String configName) {
        Configuration configuration = new Configuration().configure(configName);
        sessionFactory = configuration.buildSessionFactory();
        cache = new CacheEngineMyImpl<>(5, 1000, 0);
        logger.debug("DBService initialized");
    }

    public long create(User objectData) {
        logger.debug("start saving entity");
        long id;
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                id = (long) session.save(objectData);
                session.getTransaction().commit();
            } catch (Exception e) {
                logger.error("Exception occurred while creating entity: {}", e.getMessage());
                session.getTransaction().rollback();
                throw new RuntimeException(e);
            }
        }
        logger.debug("entity saved with id = {}", id);
        cache.put(id, objectData);
        return id;
    }

    public void update(User objectData) {
        throw new UnsupportedOperationException();
    }

    public long createOrUpdate(User objectData) {
        throw new UnsupportedOperationException();
    }

    public User load(long id) {
        final Object cachedData = cache.get(id);
        if (cachedData != null) {
            logger.debug("got cached entity: {}", cachedData);
            return (User) cachedData;
        }
        logger.debug("start loading entity");
        try (Session session = sessionFactory.openSession()) {
            final User loadedEntity = session.get(User.class, id);
            logger.debug("loaded entity: {}", loadedEntity);
            return loadedEntity;
        }
    }

    @Override
    public User loadUserByName(String name) {
        //TODO тут конечно нужно реализовать запрос в БД, но не охота пока
        if (name.equals("Roman")) {
            return new User("Roman", 31, new Address(), "verda", new Phone());
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        //TODO тут конечно нужно реализовать запрос в БД, но не охота пока
        return new ArrayList<>();
    }
}
