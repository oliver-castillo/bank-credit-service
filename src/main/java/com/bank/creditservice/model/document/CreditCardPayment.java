package com.bank.creditservice.model.document;

import com.bank.creditservice.model.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditCardPayment extends Transaction {
    public CreditCardPayment() {
        super(TransactionType.CREDIT_CARD_PAYMENT);
    }

    private String creditCardId;
}
