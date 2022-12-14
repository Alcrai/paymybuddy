package com.paymybuddy.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Entity
@Table(name = "users")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private int userId;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "username")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToMany(
            cascade = CascadeType.MERGE,
            fetch = FetchType.LAZY
    )
    @JoinTable(name="users_userfriend",
                joinColumns = @JoinColumn(name ="id_user"),
                inverseJoinColumns = @JoinColumn(name="id_userfriend"))
    private List<UserAccount> friends = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
            )
    @JoinColumn(name="id_user")
    private List<BankAccount> bankAccounts;

    public UserAccount(int userId, String lastName, String firstName, String email, String password) {
        this.userId = userId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
    }

    public UserAccount() {
    }


    public UserAccount(int userId, String lastName, String firstName, String email, String password, List<UserAccount> friends, List<BankAccount> bankAccounts) {
        this.userId = userId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.friends = friends;
        this.bankAccounts = bankAccounts;
    }

    public UserAccount(int userId, String lastName, String firstName, String email, String password, List<BankAccount> bankAccounts) {
        this.userId = userId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.bankAccounts = bankAccounts;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
