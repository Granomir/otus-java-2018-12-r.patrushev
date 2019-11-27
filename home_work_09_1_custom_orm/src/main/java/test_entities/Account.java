package test_entities;

import dbservice.Id;

import java.util.Objects;

public class Account {
    @Id
    private long no;
    private String type;
    private double rest;

    public Account(long no, String type, double rest) {
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    public Account(String type, double rest) {
        this.type = type;
        this.rest = rest;
    }

    public Account() {
    }

    public void setNo(long no) {
        this.no = no;
    }

    public long getNo() {
        return no;
    }

    public String getType() {
        return type;
    }

    public double getRest() {
        return rest;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + no +
                ", name='" + type + '\'' +
                ", age=" + rest +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account user = (Account) o;
        return no == user.no &&
                rest == user.rest &&
                Objects.equals(type, user.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(no, type, rest);
    }
}
