package dbservice.impl;

import dbservice.JDBCTemplate;
import test_entities.User;

public class DbServiceUserImpl {
    private JDBCTemplate template;

    public DbServiceUserImpl(JDBCTemplate template) {
        this.template = template;
    }

    public long saveUser(User user) {
        return template.create(user);
    }

    public void updateUser(User user) {
        template.update(user);
    }

    public User loadUser(long id) {
        return template.load(id, User.class);
    }

    public long createOrUpdateUser(User user) {
        return template.createOrUpdate(user);
    }
}
