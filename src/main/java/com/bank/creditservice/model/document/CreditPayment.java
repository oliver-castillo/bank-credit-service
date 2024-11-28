package com.bank.creditservice.model.document;

import com.bank.creditservice.model.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditPayment extends Transaction {
    public CreditPayment() {
        super(TransactionType.CREDIT_PAYMENT);
    }

    private String creditId;

    private Double interest;
}
