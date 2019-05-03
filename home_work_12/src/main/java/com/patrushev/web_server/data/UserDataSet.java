package com.patrushev.web_server.data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "userDataSet")
public class UserDataSet extends DataSet {

    @Column(name = "user_name")
    private String user_name;

    @Column(name = "age")
    private int age;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private AddressDataSet address;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<PhoneDataSet> phones = new HashSet<>();

    public UserDataSet() {
    }

    public UserDataSet(String user_name, int age, AddressDataSet address, PhoneDataSet... phones) {
        this.user_name = user_name;
        this.age = age;
        if (address != null) {
            address.setOwner(this);
        }
        this.address = address;
        if (phones != null) {
            for (PhoneDataSet phone : phones) {
                phone.setOwner(this);
                this.phones.add(phone);
            }
        }
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    public void setPhones(Set<PhoneDataSet> phones) {
        this.phones = phones;
    }

    public String getUser_name() {
        return user_name;
    }

    public int getAge() {
        return age;
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public Set<PhoneDataSet> getPhones() {
        return phones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDataSet that = (UserDataSet) o;
        return age == that.age &&
                Objects.equals(user_name, that.user_name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(phones, that.phones);
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
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }
}
