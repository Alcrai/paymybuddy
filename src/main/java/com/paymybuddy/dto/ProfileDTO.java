package com.paymybuddy.dto;

import lombok.Data;

@Data
public class ProfileDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String nameBank;
    private String iban;

    public ProfileDTO(String firstName, String lastName, String email, String password, String nameBank, String iban) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.nameBank = nameBank;
        this.iban = iban;
    }
}
