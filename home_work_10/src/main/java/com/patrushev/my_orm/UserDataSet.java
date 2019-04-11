package com.patrushev.my_orm;

import java.util.Objects;

public class UserDataSet extends DataSet {
    private String user_name;

    private int age;
    public UserDataSet() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDataSet that = (UserDataSet) o;
        return age == that.age &&
                Objects.equals(user_name, that.user_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_name, age);
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
                "user_name='" + user_name + '\'' +
                ", age=" + age +
                '}';
    }
}
