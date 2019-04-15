package com.patrushev.my_orm;

import com.patrushev.my_orm.data.DataSet;
import com.patrushev.my_orm.data.DataSetDAO;
import com.patrushev.my_orm.data.UserDataSet;
import com.patrushev.my_orm.dbutils.QueryingHelper;
import com.patrushev.my_orm.dbutils.PostgresQueryingHelper;
import com.patrushev.my_orm.utils.ConnectionHelper;
import com.patrushev.my_orm.dbutils.DBService;
import com.patrushev.my_orm.dbutils.PostgresDBService;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * в данном примере у меня connection к БД закрывается только если я удаляю созданную в начале БД (в методе dropDB),
 *         т.е. если бы я её не удалял, то подключение к БД так бы и осталось открытым до конца работы приложения.
 *         вообщем не очень ясное управление закрытием ресурсов.
 *         у меня создается коннекшн сначала к базе "postgres" для создания своей БД, который передается в DBService,
 *         и там внутри он уже обновляется после создания новой БД (mydb), закрывая старый коннекшн
 *         получается что из main я уже не могу управлять заркытием этого коннекшена.
 *         на ум приходит только добавить метод closeCurrentConnection в DBService и вызвать его в конце метода main для закрытия ресурсов.
 *         и возможно сделать метод setCurrentConnection для того, чтобы управлять всеми подключениями из метода main ->
 *         т.е. сначала создать DBService передавая ему коннекшн к postgres,
 *         после создания mydb закрыть из метода main (методом closeCurrent...) старый коннекшн и обновить его методом setCurrent...
 *         то же самое выполнить при удалении БД и в конце метода main просто закрыть connection методом closeCurrent...
 *         насколько правильно/неправильно мыслю?
 */

public class Main {
    public static void main(String[] args) throws SQLException {
        try (Connection postgresConnection = ConnectionHelper.getPostgresqlConnection()){
            DataSet savingUser = new UserDataSet("Roman", 29);
            QueryingHelper postgresQueryingHelper = new PostgresQueryingHelper();
            DataSetDAO dataSetDAO = new DataSetDAO();
            DBService postgresDBService = new PostgresDBService(postgresConnection, postgresQueryingHelper, dataSetDAO);
            postgresDBService.save(savingUser);
//            DataSet loadedUser = postgresDBService.load(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public static void main1(String[] args) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//        //подготовка БД
//        String createTableQuery = "CREATE TABLE IF NOT EXISTS userdataset (\n" +
//                "  id        BIGSERIAL NOT NULL PRIMARY KEY,\n" +
//                "  user_name VARCHAR(255),\n" +
//                "  age       INTEGER\n" +
//                ");";
//        DBService postgresDbService = new PostgresDBService(ConnectionHelper.getPostgresqlConnection(), postgresDDLHelper);
//        postgresDbService.createDB("mydb");
//        postgresDbService.createTable(createTableQuery);
//        //вставка новых объектов в БД
//        DataSetDAO dao = new DataSetDAO(postgresDbService);
//        UserDataSet user1 = new UserDataSet("roman", 29);
//        UserDataSet user2 = new UserDataSet("tatiana", 28);
//        UserDataSet user3 = new UserDataSet("anna", 4);
//        UserDataSet user4 = new UserDataSet("alexandra", 3);
//        dao.save(user1);
//        dao.save(user2);
//        dao.save(user3);
//        dao.save(user4);
//        //выгрузка элементов из БД
//        UserDataSet user11 = dao.load(1, UserDataSet.class);
//        System.out.println("user 1 equals user 11: " + user1.equals(user11));
//        UserDataSet user22 = dao.load(2, UserDataSet.class);
//        System.out.println("user 2 equals user 22: " + user2.equals(user22));
//        UserDataSet user33 = dao.load(3, UserDataSet.class);
//        System.out.println("user 3 equals user 33: " + user3.equals(user33));
//        UserDataSet user44 = dao.load(4, UserDataSet.class);
//        System.out.println("user 4 equals user 44: " + user4.equals(user44));
//        //удаление БД
//        postgresDbService.dropDB("mydb");
//    }
}
