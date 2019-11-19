package test_entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "street")
    private String street;
    @OneToOne
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private User owner;

    public Address(String street, User owner) {
        this.street = street;
        this.owner = owner;
    }

    public Address(String street) {
        this.street = street;
    }

    public Address() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
