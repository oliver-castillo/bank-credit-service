package com.bank.creditservice.model.dto.response;

import com.bank.creditservice.model.enums.ClientType;

public record CreditCardResponse(
        String id,
        String clientId,
        String cardNumber,
        String cvv,
        ClientType clientType,
        String status,
        Double creditLimit,
        Double balanceToPay,
        String expirationDate,
        Boolean isEnabled
) {
}
