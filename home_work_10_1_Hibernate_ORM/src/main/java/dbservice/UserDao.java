package dbservice;

import test_entities.User;

import java.util.List;

public interface UserDao {
    long create(User objectData);

    void update(User objectData);

    long createOrUpdate(User objectData);

    User load(long id);

    User loadByName(String name);

    List<User> getAll();
}
