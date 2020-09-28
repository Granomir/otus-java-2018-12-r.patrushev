package com.patrushev.web_server.data;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDataSetDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDataSetDAO.class);

    public <T extends DataSet> void save(Session session, T entity) {
        session.save(entity);
        logger.info("Завершено сохранение объекта в БД");
    }

    public <T extends DataSet> T load(Session session, long id, Class<T> clazz) {
        logger.info("Производится выгрузка объекта из БД");
        return session.load(clazz, id);
    }

    public UserDataSet readByName(Session session, String name) {
        logger.info("Производится выгрузка пользователя из БД по имени");
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
        Root<UserDataSet> from = criteria.from(UserDataSet.class);
        criteria.where(builder.equal(from.get("user_name"), name));
        Query<UserDataSet> query = session.createQuery(criteria);
        return query.uniqueResult();
    }

    public List<UserDataSet> readAll(Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
        criteria.from(UserDataSet.class);
        return session.createQuery(criteria).list();
    }
}
