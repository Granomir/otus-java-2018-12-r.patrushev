package com.patrushev.my_orm;

import com.patrushev.my_orm.data.*;
import com.patrushev.my_orm.dbutils.*;
import org.hibernate.cfg.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//        try (Connection postgresConnection = ConnectionHelper.getPostgresqlConnection()) {
        //создаю юзеров
        DataSet savingUser1 = new UserDataSet("Roman", 29, new AddressDataSet("P"), new PhoneDataSet("937"), new PhoneDataSet("955"));
        DataSet savingUser2 = new UserDataSet("Tatiana", 28, new AddressDataSet("M"), new PhoneDataSet("964"));
        DataSet savingUser3 = new UserDataSet("Anna", 4, new AddressDataSet("SD"), new PhoneDataSet("917"));
        DataSet savingUser4 = new UserDataSet("Aleksandra", 3, new AddressDataSet("MD"), new PhoneDataSet("915"));
        Configuration configuration = getConfiguration(UserDataSet.class, PhoneDataSet.class, AddressDataSet.class);

        //создаю DBService
        DBService postgresDBService = new DBServiceHibernateImpl(configuration);


//            QueryingHelper postgresQueryingHelper = new PostgresQueryingHelper();
//            DataSetDAO dataSetDAO = new DataSetDAO(postgresQueryingHelper);
//            DBService postgresDBService = new DBServiceImpl(postgresConnection, postgresQueryingHelper, dataSetDAO);
        postgresDBService.save(savingUser1);
        postgresDBService.save(savingUser2);
        postgresDBService.save(savingUser3);
        postgresDBService.save(savingUser4);
            DataSet loadedUser1 = postgresDBService.load(1, UserDataSet.class);
        System.out.println(loadedUser1);
            DataSet loadedUser2 = postgresDBService.load(2, UserDataSet.class);
            DataSet loadedUser3 = postgresDBService.load(3, UserDataSet.class);
            DataSet loadedUser4 = postgresDBService.load(4, UserDataSet.class);
//            System.out.println(loadedUser1.equals(savingUser1));
//            System.out.println(loadedUser2.equals(savingUser2));
//            System.out.println(loadedUser3.equals(savingUser3));
//            System.out.println(loadedUser4.equals(savingUser4));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    private static Configuration getConfiguration(Class... classes) {
        //создаю конфиг для hibernate
        Configuration configuration = new Configuration();
        //передаю в конфигурацию классы, с которыми будет работать хибернейт
        for (Class aClass : classes) {
            configuration.addAnnotatedClass(aClass);
        }
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            //устанавливю параметры работы хибернейта
            //диалект
            configuration.setProperty("hibernate.dialect", prop.getProperty("dialect"));
            //драйвер
            configuration.setProperty("hibernate.connection.driver_class", prop.getProperty("driverClass"));
            //адрес
            configuration.setProperty("hibernate.connection.url", prop.getProperty("url"));
            //user
            configuration.setProperty("hibernate.connection.username", prop.getProperty("username"));
            //pass - в моем случае не нужен
            //configuration.setProperty("hibernate.connection.password", "tully");
            //будет показывать сформированные sql запросы
            configuration.setProperty("hibernate.show_sql", prop.getProperty("show_sql"));
            //здесь задается, что Хибернейт сам подготовит БД для работы с передаными ему классами - удалит соответсвующие таблицы, если они есть и создаст снова
            configuration.setProperty("hibernate.hbm2ddl.auto", prop.getProperty("hbm2ddl.auto"));
            //не используется SSL
            configuration.setProperty("hibernate.connection.useSSL", prop.getProperty("useSSL"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configuration;
    }
}
