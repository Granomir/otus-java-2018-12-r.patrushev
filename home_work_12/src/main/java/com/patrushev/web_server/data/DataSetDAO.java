package com.patrushev.web_server.data;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSetDAO {
    private static Logger logger = LoggerFactory.getLogger(DataSetDAO.class);

    public <T extends DataSet> void save(Session session, T entity) {
        session.save(entity);
        logger.info("Завершено сохранение объекта в БД");
    }

    public <T extends DataSet> T load(Session session, long id, Class<T> clazz) {
        logger.info("Производится выгрузка объекта из БД");
        return session.load(clazz, id);
    }
}
