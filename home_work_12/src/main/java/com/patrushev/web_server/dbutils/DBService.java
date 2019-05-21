package com.patrushev.web_server.dbutils;

import com.patrushev.web_server.data.DataSet;
import com.patrushev.web_server.data.UserDataSet;

import java.util.List;
import java.util.Map;

public interface DBService extends AutoCloseable {

    <T extends DataSet> void save(T entity);

    <T extends DataSet> T load(long id, Class<T> clazz);

    UserDataSet loadUserByName(String login);

    List<UserDataSet> getAllUsers();
}
