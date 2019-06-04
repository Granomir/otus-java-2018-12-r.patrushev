package com.patrushev.web_server.dbutils;

import com.patrushev.web_server.data.DataSet;
import com.patrushev.web_server.data.UserDataSet;
import com.patrushev.web_server.messageSystem.Addressee;

import java.util.List;

public interface DBService extends AutoCloseable, Addressee {

    <T extends DataSet> void save(T entity);

    <T extends DataSet> T load(long id, Class<T> clazz);

    UserDataSet loadUserByName(String login);

    List<UserDataSet> getAllUsers();
}
