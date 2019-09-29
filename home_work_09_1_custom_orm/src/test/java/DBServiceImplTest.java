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
        System.out.println("start creating tables");
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
             PreparedStatement pst1 = connection.prepareStatement("CREATE TABLE User(id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3))");
             PreparedStatement pst2 = connection.prepareStatement("CREATE TABLE Account(no bigint(20) NOT NULL auto_increment, type varchar(255), rest double)")) {
            pst1.executeUpdate();
            pst2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("finish creating tables");
    }

    @AfterEach
    void tearDown() {
        System.out.println("start dropping tables");
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
             PreparedStatement pst1 = connection.prepareStatement("DROP TABLE IF EXISTS User");
             PreparedStatement pst2 = connection.prepareStatement("DROP TABLE IF EXISTS Account")) {
            pst1.executeUpdate();
            pst2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("finish dropping tables");
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
        DBService dbService = new DBServiceImpl(new DbExecutorImpl(), new DataSourceH2());
        User user1 = new User("Roman", 29);
        long id1 = dbService.createOrUpdate(user1);
        User user2 = new User(id1, "Roman", 29);
        assertEquals(user2, dbService.load(id1, user2.getClass()));
        User user3 = new User(id1, "Roman", 30);
        long id2 = dbService.createOrUpdate(user3);
        assertEquals(id1, id2);
        assertEquals(user3, dbService.load(id1, user3.getClass()));
        User user4 = new User(50, "Roman", 31);
        long id3 = dbService.createOrUpdate(user4);
        user4.setId(id3);
        assertEquals(user4, dbService.load(id3, user4.getClass()));
    }
}