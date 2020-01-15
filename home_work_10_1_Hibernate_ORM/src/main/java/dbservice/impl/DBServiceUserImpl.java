package dbservice.impl;

import dbservice.DBService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test_entities.User;

public class DBServiceUserImpl implements DBService<User> {
    private Logger logger = LoggerFactory.getLogger(DBServiceUserImpl.class);

    private final SessionFactory sessionFactory;

    public DBServiceUserImpl() {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
        logger.debug("DBService initialized");
    }

    public DBServiceUserImpl(String configName) {
        Configuration configuration = new Configuration().configure(configName);
        sessionFactory = configuration.buildSessionFactory();
        logger.debug("DBService initialized");
    }

    public long create(User objectData) {
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
        return id;
    }

    public void update(User objectData) {

    }

    public long createOrUpdate(User objectData) {
        return 0;
    }

    public User load(long id) {
        logger.debug("start loading entity");
        try (Session session = sessionFactory.openSession()) {
            final User loadedEntity = session.get(User.class, id);
            logger.debug("loaded entity: {}", loadedEntity);
            return loadedEntity;
        }
    }
}
