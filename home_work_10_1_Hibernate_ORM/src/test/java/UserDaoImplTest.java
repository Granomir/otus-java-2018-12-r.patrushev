import dbservice.UserDao;
import dbservice.impl.UserDaoImpl;
import org.junit.jupiter.api.Test;
import test_entities.Address;
import test_entities.Phone;
import test_entities.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDaoImplTest {

    @Test
    void testCreateAndLoad() {
        UserDao userDao = new UserDaoImpl();
        User user1 = new User("Roman", 29, new Address("Lavrinenko"), "verda", new Phone("79375292927"), new Phone("79153527806"));
        long id = userDao.create(user1);
        assertEquals(user1, userDao.load(id));
    }
}