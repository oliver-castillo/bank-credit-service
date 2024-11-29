package com.bank.creditservice.model.dto.response;

public record CreditCardResponse(
        String id,
        String clientId,
        String cardNumber,
        String cvv,
        String creditCardStatus,
        Double creditLimit,
        String expirationDate,
        Boolean isEnabled
) {
}
