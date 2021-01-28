package dao;

import test_entities.Account;

public interface AccountDao {
    long create(Account objectData);

    void update(Account objectData);

    long createOrUpdate(Account objectData);

    Account load(long id);
}
