package com.bank.creditservice.model.dto.response;

import java.time.LocalDate;

public record CreditResponse(
        String id,
        String clientId,
        String cardNumber,
        String cvv,
        String clientType,
        String status,
        Double creditLimit,
        Double balanceToPay,
        LocalDate expirationDate,
        Boolean isEnabled
) {
}
