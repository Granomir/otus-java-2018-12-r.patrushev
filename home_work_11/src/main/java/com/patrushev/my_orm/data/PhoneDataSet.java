package com.patrushev.my_orm.data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "phone")
public class PhoneDataSet extends DataSet {
    @Column(name = "number")
    private String number;

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private UserDataSet owner;

    public PhoneDataSet(String number) {
        this.number = number;
    }

    public PhoneDataSet() {
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public UserDataSet getOwner() {
        return owner;
    }

    public String getNumber() {
        return number;
    }

    public void setOwner(UserDataSet user) {
        owner = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneDataSet that = (PhoneDataSet) o;
        return Objects.equals(number, that.number) &&
                Objects.equals(owner, that.owner);
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(number, owner);
//    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "number='" + number + '\'' +
                ", owner=" + owner +
                '}';
    }
}
