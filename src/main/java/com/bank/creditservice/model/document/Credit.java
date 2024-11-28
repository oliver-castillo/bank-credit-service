package com.bank.creditservice.model.document;

import com.bank.creditservice.model.enums.ClientType;
import com.bank.creditservice.model.enums.CreditStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "credits")
public class Credit {
    @Id
    private String id;
    private String clientId;
    private Double amount;
    private Double annualInterestRate; // Tasa de interés anual en porcentaje
    private ClientType clientType; // PERSONAL - BUSINESS
    private CreditStatus status;
    private Integer numberOfPayments;

    public Credit(Double amount) {
        this.amount = Math.round(amount * 100.00) / 100.00;
        this.status = CreditStatus.ACTIVE;
    }

    public Double calculateInterest() {
        return Math.round((annualInterestRate / 12 * amount / 100) * 100.0) / 100.0;
    }

    public Double calculateMonthlyPaymentAmount() {
        double result = (amount / numberOfPayments) + calculateInterest();
        return Math.round(result * 100.0) / 100.0;
    }


    public CreditStatus checkStatus(Integer paymentsMade) {
        return paymentsMade > 0 ? CreditStatus.ACTIVE : CreditStatus.PAID;
    }
}