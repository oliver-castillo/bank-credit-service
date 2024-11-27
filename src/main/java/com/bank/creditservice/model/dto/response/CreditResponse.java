package com.bank.creditservice.model.dto.response;

import com.bank.creditservice.model.enums.ClientType;
import com.bank.creditservice.model.enums.CreditStatus;

import java.time.LocalDate;

public record CreditResponse(
        String id,

        String clientId,

        Double amount,

        Double interestRate,

        LocalDate grantDate,

        LocalDate dueDate,

        ClientType clientType,

        CreditStatus status,

        Double remainingPayment,

        Double latePaymentInterestRate
) {
}
