import dao.AccountDao;
import dao.UserDao;
import dbservice.JDBCTemplate;
import dao.AccountDaoImpl;
import dao.UserDaoImpl;
import dbservice.impl.JDBCTemplateImpl;
import dbservice.impl.DbExecutorImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test_datasorce.DataSource;
import test_datasorce.DataSourceH2;
import test_entities.Account;
import test_entities.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class JDBCTemplateImplTest {

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
        DataSource dataSource = new DataSourceH2();
        JDBCTemplate jdbcTemplate = new JDBCTemplateImpl(new DbExecutorImpl(dataSource));
        UserDao userDao = new UserDaoImpl(jdbcTemplate);
        User user1 = new User("Roman", 29);
        long id = userDao.create(user1);
        User user2 = new User(id, "Roman", 29);
        assertEquals(user2, userDao.load(id));

        AccountDao accountDao = new AccountDaoImpl(jdbcTemplate);
        Account acc1 = new Account("new", 29.13);
        long no = accountDao.create(acc1);
        Account acc2 = new Account(no, "new", 29.13);
        assertEquals(acc2, accountDao.load(no));
    }

    @Test
    void update() {
        DataSource dataSource = new DataSourceH2();
        JDBCTemplate jdbcTemplate = new JDBCTemplateImpl(new DbExecutorImpl(dataSource));
        UserDao userDao = new UserDaoImpl(jdbcTemplate);
        User user1 = new User("Roman", 29);
        long id = userDao.create(user1);
        User user2 = new User(id, "Roman", 30);
        userDao.update(user2);
        assertEquals(user2, userDao.load(id));

        AccountDao accountDao = new AccountDaoImpl(jdbcTemplate);
        Account acc1 = new Account("new", 29.13);
        long no = accountDao.create(acc1);
        Account acc2 = new Account(no, "new", 30.13);
        accountDao.update(acc2);
        assertEquals(acc2, accountDao.load(no));
    }

    @Test
    void load() {
    }

    @Test
    void createOrUpdate() {
        DataSource dataSource = new DataSourceH2();
        JDBCTemplate jdbcTemplate = new JDBCTemplateImpl(new DbExecutorImpl(dataSource));
        UserDao userDao = new UserDaoImpl(jdbcTemplate);
        User user1 = new User("Roman", 29);
        long id1 = userDao.createOrUpdate(user1);
        User user2 = new User(id1, "Roman", 29);
        assertEquals(user2, userDao.load(id1));
        User user3 = new User(id1, "Roman", 30);
        long id2 = userDao.createOrUpdate(user3);
        assertEquals(id1, id2);
        assertEquals(user3, userDao.load(id1));
        User user4 = new User(50, "Roman", 31);
        long id3 = userDao.createOrUpdate(user4);
        user4.setId(id3);
        assertEquals(user4, userDao.load(id3));

        AccountDao accountDao = new AccountDaoImpl(jdbcTemplate);
        Account acc1 = new Account("new", 29.13);
        long no1 = accountDao.createOrUpdate(acc1);
        Account acc2 = new Account(no1, "new", 29.13);
        assertEquals(acc2, accountDao.load(no1));
        Account acc3 = new Account(no1, "new", 30.13);
        long no2 = accountDao.createOrUpdate(acc3);
        assertEquals(no1, no2);
        assertEquals(acc3, accountDao.load(no1));
        Account acc4 = new Account(50, "new", 31.13);
        long no3 = accountDao.createOrUpdate(acc4);
        acc4.setNo(no3);
        assertEquals(acc4, accountDao.load(no3));
    }
}