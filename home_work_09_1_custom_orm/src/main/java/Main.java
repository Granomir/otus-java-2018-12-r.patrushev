import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        createUserTable();
        DBService dbService = new DBServiceImpl(new DbExecutorImpl(connection), dataSource);
        User user1 = new User(1, "roman", 29);
        dbService.create(user1);
        System.out.println("Проверка добавления записи в БД пройдена: " + user1.equals(dbService.load(1, User.class)));
        User user2 = new User(1, "tatiana", 29);
        dbService.update(user2);
        System.out.println("Проверка обновления записи в БД пройдена: " + user2.equals(dbService.load(1, User.class)));
        User user3 = new User(2, "anna", 5);
        dbService.createOrUpdate(user3);
        System.out.println("Проверка №1 добавления/обновления записи в БД пройдена: " + user3.equals(dbService.load(2, User.class)));
        User user4 = new User(2, "alexandra", 3);
        dbService.createOrUpdate(user4);
        System.out.println("Проверка №2 добавления/обновления записи в БД пройдена: " + user4.equals(dbService.load(2, User.class)));
    }

    private static void createUserTable() {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:");
             PreparedStatement pst = connection.prepareStatement("CREATE TABLE User(id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3))")) {
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
