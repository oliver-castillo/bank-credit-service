package com.bank.creditservice.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreditPaymentRequest extends TransactionRequest {
    @NotNull
    private String creditId;

    public CreditPaymentRequest(Double amount) {
        super(amount);
    }
}
