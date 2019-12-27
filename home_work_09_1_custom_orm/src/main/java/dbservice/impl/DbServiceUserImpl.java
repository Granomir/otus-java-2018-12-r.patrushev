package dbservice.impl;

import dbservice.DBService;
import dbservice.JDBCTemplate;
import test_entities.User;

public class DbServiceUserImpl implements DBService<User> {
    private JDBCTemplate template;

    public DbServiceUserImpl(JDBCTemplate template) {
        this.template = template;
    }

    public long create(User user) {
        return template.create(user);
    }

    public void update(User user) {
        template.update(user);
    }

    public User load(long id) {
        return template.load(id, User.class);
    }

    public long createOrUpdate(User user) {
        return template.createOrUpdate(user);
    }
}
