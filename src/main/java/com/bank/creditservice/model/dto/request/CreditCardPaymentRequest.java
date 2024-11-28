package com.bank.creditservice.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreditCardPaymentRequest extends TransactionRequest {
    @NotNull
    private String creditCardNumber;

    public CreditCardPaymentRequest(Double amount) {
        super(amount);
    }
}
