package dbservice.impl;

import dbservice.DBService;
import dbservice.JDBCTemplate;
import test_entities.User;

public class DbServiceUserImpl implements DBService<User> {
    private final JDBCTemplate template;

    public DbServiceUserImpl(JDBCTemplate template) {
        this.template = template;
    }

    @Override
    public long create(User user) {
        return template.create(user);
    }

    @Override
    public void update(User user) {
        template.update(user);
    }

    @Override
    public User load(long id) {
        return template.load(id, User.class);
    }

    @Override
    public long createOrUpdate(User user) {
        return template.createOrUpdate(user);
    }
}
