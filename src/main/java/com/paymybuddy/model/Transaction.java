package com.paymybuddy.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
@Data
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "id_transaction")
    private int transactionId;

    @Column(name = "id_account_send")
    private int accountSendId;

    @Column(name = "id_account_receive")
    private int accountReceiveId;

    @Column(name = "amount")
    private float amount;

    @Column(name = "comment")
    private String comment;

    public Transaction(int transactionId, int accountSendId, int accountReceiveId, float amount, String comment) {
        this.transactionId = transactionId;
        this.accountSendId = accountSendId;
        this.accountReceiveId = accountReceiveId;
        this.amount = amount;
        this.comment = comment;
    }

    public Transaction() {
    }
}
