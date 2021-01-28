package dao;

import test_entities.User;

public interface UserDao {
    long create(User objectData);

    void update(User objectData);

    long createOrUpdate(User objectData);

    User load(long id);
}
