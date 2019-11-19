package test_entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "phone")
public class Phone {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "number")
    private String number;
    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private User owner;

    public Phone(String number, User owner) {
        this.number = number;
        this.owner = owner;
    }

    public Phone(String number) {
        this.number = number;
    }

    public Phone() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(number, phone.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
