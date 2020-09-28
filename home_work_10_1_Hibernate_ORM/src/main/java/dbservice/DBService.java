package dbservice;

import test_entities.User;

import java.util.List;

public interface DBService<T> {
    long create(T objectData);

    void update(T objectData);

    long createOrUpdate(T objectData);

    T load(long id);

    T loadUserByName(String name);

    List<User> getAllUsers();
}
