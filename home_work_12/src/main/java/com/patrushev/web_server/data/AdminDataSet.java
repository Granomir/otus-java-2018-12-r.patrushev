package com.patrushev.web_server.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "adminDataSet")
public class AdminDataSet extends DataSet {
    @Column(name = "login")
    private String login;
    @Column(name = "pass")
    private String pass;

    public AdminDataSet() {
    }

    public AdminDataSet(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "AdminDataSet{" +
                "login='" + login + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminDataSet that = (AdminDataSet) o;
        return Objects.equals(login, that.login) &&
                Objects.equals(pass, that.pass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, pass);
    }
}
