package com.bank.creditservice.model.dto.response;

import com.bank.creditservice.model.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TransactionResponse(
        String id,
        TransactionType type,
        String clientId,
        Double amount,
        String creditCardNumber,
        String creditId,
        Double interest
) {
}
