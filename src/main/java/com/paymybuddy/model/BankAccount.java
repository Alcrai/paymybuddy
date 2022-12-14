package com.paymybuddy.model;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name = "bankaccount")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_bankaccount")
    private int bankAccountId;
    @Column(name="id_user")
    private int userId;
    @Column(name="name")
    private String name;
    @Column(name = "iban")
    private String iban;
    @Column(name="inside")
    private Boolean inside;

    public BankAccount(int bankAccountId, int userId, String name, String iban, Boolean inside) {
        this.bankAccountId = bankAccountId;
        this.userId = userId;
        this.name = name;
        this.iban = iban;
        this.inside = inside;
    }

    public BankAccount() {

    }
}
