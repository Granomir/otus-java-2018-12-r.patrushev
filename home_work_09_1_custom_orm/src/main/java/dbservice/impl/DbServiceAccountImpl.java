package dbservice.impl;

import dbservice.DBService;
import dbservice.JDBCTemplate;
import test_entities.Account;
import test_entities.User;

public class DbServiceAccountImpl implements DBService<Account> {
    private JDBCTemplate template;

    public DbServiceAccountImpl(JDBCTemplate template) {
        this.template = template;
    }

    @Override
    public long create(Account account) {
        return template.create(account);
    }

    @Override
    public void update(Account account) {
        template.update(account);
    }

    @Override
    public Account load(long id) {
        return template.load(id, Account.class);
    }

    @Override
    public long createOrUpdate(Account account) {
        return template.createOrUpdate(account);
    }
}
