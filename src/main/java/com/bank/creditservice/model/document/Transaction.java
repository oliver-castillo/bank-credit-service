package com.bank.creditservice.model.document;

import com.bank.creditservice.model.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "transactions")
public abstract class Transaction {
    @Id
    private String id;
    private TransactionType type;
    private String clientId;
    private Double amount;

    public Transaction(TransactionType type) {
        this.type = type;
    }
}