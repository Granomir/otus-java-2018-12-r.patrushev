package com.patrushev.web_server;

import com.patrushev.web_server.data.*;
import com.patrushev.web_server.dbutils.DBService;
import com.patrushev.web_server.dbutils.DBServiceHibernateImpl;
import org.hibernate.cfg.Configuration;

public class MainORM {
    public static void main(String[] args) {
        //создаю юзеров
        UserDataSet savingUser1 = new UserDataSet("Roman", 29, new AddressDataSet("P"), new PhoneDataSet("937"), new PhoneDataSet("955"));
        UserDataSet savingUser2 = new UserDataSet("Tatiana", 28, new AddressDataSet("M"), new PhoneDataSet("964"));
        UserDataSet savingUser3 = new UserDataSet("Anna", 4, new AddressDataSet("SD"), new PhoneDataSet("917"));
        UserDataSet savingUser4 = new UserDataSet("Aleksandra", 3, new AddressDataSet("MD"), new PhoneDataSet("915"));

        //создаю конфигурацию на основе конфигурационного файла xml
        Configuration configuration = new Configuration();
        configuration.configure();

        try (DBService postgresDBService = new DBServiceHibernateImpl(configuration, new DataSetDAO())) {
            //сохраняю юзеров в БД
            postgresDBService.save(savingUser1);
            postgresDBService.save(savingUser2);
            postgresDBService.save(savingUser3);
            postgresDBService.save(savingUser4);

            //достаю юзеров из БД
            DataSet loadedUser1 = postgresDBService.load(1, UserDataSet.class);
            DataSet loadedUser2 = postgresDBService.load(2, UserDataSet.class);
            DataSet loadedUser3 = postgresDBService.load(3, UserDataSet.class);
            DataSet loadedUser4 = postgresDBService.load(4, UserDataSet.class);
            System.out.println(loadedUser1.equals(savingUser1));
            System.out.println(loadedUser2.equals(savingUser2));
            System.out.println(loadedUser3.equals(savingUser3));
            System.out.println(loadedUser4.equals(savingUser4));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
