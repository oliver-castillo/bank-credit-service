package com.bank.creditservice.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public abstract class TransactionRequest {
    @NotNull
    private String clientId;
    @NotNull
    private Double amount;

    public TransactionRequest(Double amount) {
        this.amount = Math.round(amount * 100.00) / 100.00;
    }
}
