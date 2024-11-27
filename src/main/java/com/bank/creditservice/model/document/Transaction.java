package com.bank.creditservice.model.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Getter
@Setter
public class Transaction {
    @Id
    private String id;

    private String type = "CREDIT_PAYMENT";

    private String creditId;

    private String clientId;

    private double amount;

    private String interest;
}