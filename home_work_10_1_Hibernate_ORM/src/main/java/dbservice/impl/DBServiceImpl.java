package dbservice.impl;

import dbservice.DBService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBServiceImpl implements DBService {
    private Logger logger = LoggerFactory.getLogger(DBServiceImpl.class);

    private final SessionFactory sessionFactory;

    public DBServiceImpl() {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
        logger.debug("DBService initialized");
    }

    public DBServiceImpl(String configName) {
        Configuration configuration = new Configuration().configure(configName);
        sessionFactory = configuration.buildSessionFactory();
        logger.debug("DBService initialized");
    }

    public <T> long create(T objectData) {
        logger.debug("start saving entity");
        long id = -1;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            id = (long) session.save(objectData);
            session.getTransaction().commit();
        }
        logger.debug("entity saved with id = {}", id);
        return id;
    }

    public <T> void update(T objectData) {

    }

    public <T> long createOrUpdate(T objectData) {
        return 0;
    }

    public <T> T load(long id, Class<T> clazz) {
        logger.debug("start loading entity");
        try (Session session = sessionFactory.openSession()) {
            final T loadedEntity = session.get(clazz, id);
            logger.debug("loaded entity: {}", loadedEntity);
            return loadedEntity;
        }
    }
}
