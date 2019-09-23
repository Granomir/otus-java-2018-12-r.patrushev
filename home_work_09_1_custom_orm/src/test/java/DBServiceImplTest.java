import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DBServiceImplTest {

    @BeforeEach
    void setUp() {
        System.out.println("start creating table");
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
             PreparedStatement pst = connection.prepareStatement("CREATE TABLE User(id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3))")) {
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("finish creating table");
    }

    @AfterEach
    void tearDown() {
        System.out.println("start dropping table");
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
             PreparedStatement pst = connection.prepareStatement("DROP TABLE IF EXISTS User")) {
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("finish dropping table");
    }

    @Test
    void testCreateAndLoad() {
        DBService dbService = new DBServiceImpl(new DbExecutorImpl(), new DataSourceH2());
        User user1 = new User("Roman", 29);
        long id = dbService.create(user1);
        User user2 = new User(id, "Roman", 29);
        assertEquals(user2, dbService.load(id, user2.getClass()));
    }

    @Test
    void update() {
        DBService dbService = new DBServiceImpl(new DbExecutorImpl(), new DataSourceH2());
        User user1 = new User("Roman", 29);
        long id = dbService.create(user1);
        User user2 = new User(id, "Roman", 30);
        dbService.update(user2);
        assertEquals(user2, dbService.load(id, user2.getClass()));
    }

    @Test
    void load() {
    }

    @Test
    void createOrUpdate() {
    }
}