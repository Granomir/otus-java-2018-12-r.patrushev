package test_entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private int age;
    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    private Address address;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Phone> phones = new HashSet<>();
    @Column(name = "pass")
    private String pass;

    public User(long id, String name, int age, Address address, String pass, Phone... phones) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
        this.pass = pass;
        address.setOwner(this);
        for (Phone phone : phones) {
            phone.setOwner(this);
            this.phones.add(phone);
        }
    }

    public User(String name, int age, Address address, String pass, Phone... phones) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.pass = pass;
        address.setOwner(this);
        for (Phone phone : phones) {
            phone.setOwner(this);
            this.phones.add(phone);
        }
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public void setPhones(Set<Phone> phones) {
        for (Phone phone : phones) {
            phone.setOwner(this);
        }
        this.phones = phones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age &&
                Objects.equals(name, user.name) &&
                Objects.equals(address, user.address) &&
                Objects.equals(phones, user.phones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, address, phones);
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }
}
