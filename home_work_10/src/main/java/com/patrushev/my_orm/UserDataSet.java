package com.patrushev.my_orm;

//конкретная сущность User, хранящаяся в БД - строка в БД
public class UserDataSet extends DataSet {
    private String user_name;
    private int age;

    public UserDataSet(String user_name, int age) {
        this.user_name = user_name;
        this.age = age;
    }

    public String getUser_name() {
        return user_name;
    }

    public int getAge() {
        return age;
    }
}
