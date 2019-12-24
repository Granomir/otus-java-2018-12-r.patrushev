package dbservice.impl;

import com.patrushev.my_cache_engine.CacheEngine;
import com.patrushev.my_cache_engine.CacheEngineMyImpl;
import dbservice.DBService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBServiceImpl implements DBService {
    private Logger logger = LoggerFactory.getLogger(DBServiceImpl.class);

    private final SessionFactory sessionFactory;
    private final CacheEngine<Long, Object> cache;

    public DBServiceImpl() {
        this("hibernate.cfg.xml");
    }

    public DBServiceImpl(String configName) {
        Configuration configuration = new Configuration().configure(configName);
        sessionFactory = configuration.buildSessionFactory();
        cache = new CacheEngineMyImpl<>(5, 1000, 0);
        logger.debug("DBService initialized");
    }

    public <T> long create(T objectData) {
        logger.debug("start saving entity");
        long id = -1;
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

    public <T> void update(T objectData) {

    }

    public <T> long createOrUpdate(T objectData) {
        return 0;
    }

    @SuppressWarnings("unchecked")
    public <T> T load(long id, Class<T> clazz) {
        final Object cachedData = cache.get(id);
        if (cachedData != null) {
            logger.debug("got cached entity: {}", cachedData);
            return (T) cachedData;
        }
        logger.debug("start loading entity");
        try (Session session = sessionFactory.openSession()) {
            final T loadedEntity = session.get(clazz, id);
            logger.debug("loaded entity: {}", loadedEntity);
            return loadedEntity;
        }
    }
}
