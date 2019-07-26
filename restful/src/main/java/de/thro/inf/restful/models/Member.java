package de.thro.inf.restful.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
public class Member {

    @Id
    private String nickName;

    private String firstName;
    private String lastName;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "ownerCl", cascade = CascadeType.ALL)
    private List<Clothes> clothes = new LinkedList<>();

    @Version
    private Long version;

    @NotNull
    private String password;

    @Email
    @NotNull
    private String mail;

    @NotNull
    private Long credit;


    public Member() {
    }

    public Member(String nickName, String firstName, String lastName, Address address,
                  Long version, @NotNull String password, @Email @NotNull String mail,
                  @NotNull Long credit) {
        this.nickName = nickName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.version = version;
        this.password = password;
        this.mail = mail;
        this.credit = credit;
    }

    public Member(String firstName, String lastname) {
        this.firstName = firstName;
        this.lastName = lastname;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Clothes> getClothes() {
        return clothes;
    }

    public void setClothes(List<Clothes> clothes) {
        this.clothes = clothes;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Long getCredit() {
        return credit;
    }

    public void setCredit(Long credit) {
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "Member{" +
                "nickname=" + nickName +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return nickName.equals(member.nickName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickName);
    }
}