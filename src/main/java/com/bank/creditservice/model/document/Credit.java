package com.bank.creditservice.model.document;

import com.bank.creditservice.model.enums.ClientType;
import com.bank.creditservice.model.enums.CreditStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Document(collection = "credits")
public class Credit {
    @Id
    private String id;

    private String clientId;

    private double amount;

    private double interestRate; // Tasa de interés anual en porcentaje

    private LocalDate grantDate;

    private LocalDate dueDate;

    private ClientType clientType; // PERSONAL - BUSINESS

    private CreditStatus status;

    private double remainingPayment;

    private double latePaymentInterestRate;

    public Credit(double amount) {
        this.amount = amount;
        this.remainingPayment = amount;
        this.status = CreditStatus.ACTIVE;
    }

    public double calculateInterest() {
        return (interestRate / 12) * remainingPayment / 100;
    }

    public double calculateLatePaymentInterest() {
        return dueDate.isAfter(LocalDate.now())
                ? (latePaymentInterestRate / 12) * remainingPayment / 100
                : 0;
    }
}