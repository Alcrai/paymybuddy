package com.paymybuddy.dto;

import lombok.Data;

@Data
public class TabTransactionDTO {
    private String name;
    private String comment;
    private float amount;

    public TabTransactionDTO(String name, String comment, float amount) {
        this.name = name;
        this.comment = comment;
        this.amount = amount;
    }
}
