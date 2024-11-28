package com.bank.creditservice.model.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ChargeRequest extends TransactionRequest {
    private String creditCardId;

    public ChargeRequest(Double amount) {
        super(amount);
    }
}
