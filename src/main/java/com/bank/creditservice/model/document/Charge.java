package com.bank.creditservice.model.document;

import com.bank.creditservice.model.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Charge extends Transaction {
    public Charge() {
        super(TransactionType.CREDIT_CARD_CHARGE);
    }

    private String creditCardId;
}
