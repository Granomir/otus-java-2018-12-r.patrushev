package com.patrushev.my_orm.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateSessionFactory {
    private static Logger logger = LoggerFactory.getLogger(HibernateSessionFactory.class);

    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        logger.info("Инициализировано создание SessionFactory");
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // забирает параметры из файла hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new ExceptionInInitializerError("Ошибка инициализации SessionFactory" + e);
        }
        logger.info("Создание SessionFactory завершено");
        return sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        logger.info("Запрошена SessionFactory");
        return sessionFactory;
    }

}
