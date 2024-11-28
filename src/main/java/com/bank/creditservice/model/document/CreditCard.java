package com.bank.creditservice.model.document;

import com.bank.creditservice.model.enums.ClientType;
import com.bank.creditservice.model.enums.CreditCardStatus;
import com.bank.creditservice.util.Generator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Setter
@Getter
@Document(collection = "credit-cards")
public class CreditCard {
    @Id
    private String id;
    private String clientId;
    private String cardNumber;
    private String cvv;
    private ClientType clientType;
    private CreditCardStatus status;
    private Double creditLimit;
    private Double balanceToPay;
    private LocalDate expirationDate;
    private Boolean isEnabled;

    public CreditCard(double creditLimit) {
        this.creditLimit = creditLimit;
        this.balanceToPay = creditLimit;
        this.cardNumber = Generator.generateCardNumber();
        this.cvv = Generator.generateCvv();
        this.expirationDate = LocalDate.now().plusYears(1);
        this.isEnabled = true;
        this.status = CreditCardStatus.ACTIVE;
    }
}
