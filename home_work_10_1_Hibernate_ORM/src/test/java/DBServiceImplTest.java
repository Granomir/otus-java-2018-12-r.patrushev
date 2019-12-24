import dbservice.DBService;
import dbservice.impl.DBServiceImpl;
import org.junit.jupiter.api.Test;
import test_entities.Address;
import test_entities.Phone;
import test_entities.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DBServiceImplTest {

    @Test
    void testCreateAndLoad() {
        DBService dbService = new DBServiceImpl();
        User user1 = new User("Roman", 29, new Address("Lavrinenko"), new Phone("79375292927"), new Phone("79153527806"));
        long id = dbService.create(user1);
        User user2 = new User(id, "Roman", 29, new Address("Lavrinenko"), new Phone("79375292927"), new Phone("79153527806"));
        assertEquals(user2, dbService.load(id, user2.getClass()));
        final Address lavrinenko = new Address("Lavrinenko");
        long id1 = dbService.create(lavrinenko);
        assertEquals(lavrinenko, dbService.load(id1, lavrinenko.getClass()));
    }
}