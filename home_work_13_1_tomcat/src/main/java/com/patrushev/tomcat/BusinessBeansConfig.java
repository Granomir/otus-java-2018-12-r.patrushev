package com.patrushev.tomcat;

import com.patrushev.my_cache_engine.CacheEngine;
import com.patrushev.my_cache_engine.CacheEngineMyImpl;
import dbservice.UserDao;
import dbservice.impl.UserDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusinessBeansConfig {
    @Bean
    public UserDao userDao() {
        return new UserDaoImpl("hibernate.cfg.xml", cacheEngine());
    }

    public CacheEngine<Long, Object> cacheEngine() {
        return new CacheEngineMyImpl<>(5, 1000, 0);
    }
}
