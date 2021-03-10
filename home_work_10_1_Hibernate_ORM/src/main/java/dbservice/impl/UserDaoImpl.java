package dbservice.impl;

import com.patrushev.my_cache_engine.CacheEngine;
import com.patrushev.my_cache_engine.CacheEngineMyImpl;
import dbservice.UserDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test_entities.Address;
import test_entities.Phone;
import test_entities.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    private final SessionFactory sessionFactory;
    private final CacheEngine<Long, Object> cache;

    public UserDaoImpl() {
        this("hibernate.cfg.xml");
    }

    public UserDaoImpl(String configName) {
        Configuration configuration = new Configuration().configure(configName);
        sessionFactory = configuration.buildSessionFactory();
        cache = new CacheEngineMyImpl<>(5, 1000, 0);
        log.debug("DBService initialized");
    }

    public UserDaoImpl(String configName, CacheEngine<Long, Object> cache ) {
        Configuration configuration = new Configuration().configure(configName);
        sessionFactory = configuration.buildSessionFactory();
        this.cache = cache;
        log.debug("DBService initialized");
    }

    public long create(User objectData) {
        log.debug("start saving entity");
        long id;
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                id = (long) session.save(objectData);
                session.getTransaction().commit();
            } catch (Exception e) {
                log.error("Exception occurred while creating entity: {}", e.getMessage());
                session.getTransaction().rollback();
                throw new RuntimeException(e);
            }
        }
        log.debug("entity saved with id = {}", id);
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
            log.debug("got cached entity: {}", cachedData);
            return (User) cachedData;
        }
        log.debug("start loading entity");
        try (Session session = sessionFactory.openSession()) {
            final User loadedEntity = session.get(User.class, id);
            log.debug("loaded entity: {}", loadedEntity);
            cache.put(id, loadedEntity);
            return loadedEntity;
        }
    }

    @Override
    public User loadByName(String name) {
        //TODO тут конечно нужно реализовать запрос в БД, но не охота пока
        if (name.equals("Roman")) {
            return new User("Roman", 31, new Address(), "verda", new Phone());
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        log.debug("start getting all users");
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            criteria.from(User.class);
            return session.createQuery(criteria).getResultList();
        }
    }
}
