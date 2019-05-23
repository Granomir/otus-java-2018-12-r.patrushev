package com.patrushev.my_orm.data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "address")
public class AddressDataSet extends DataSet {

    @Column(name = "street")
    private String street;

    @OneToOne
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private UserDataSet owner;

    public AddressDataSet(String street) {
        this.street = street;
    }

    public AddressDataSet() {
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public UserDataSet getOwner() {
        return owner;
    }


    public String getStreet() {
        return street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDataSet that = (AddressDataSet) o;
        return Objects.equals(street, that.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street);
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "street='" + street + '\'' +
                '}';
    }

    public void setOwner(UserDataSet user) {
        owner = user;
    }
}
