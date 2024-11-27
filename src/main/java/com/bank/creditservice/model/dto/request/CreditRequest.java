package com.bank.creditservice.model.dto.request;

import com.bank.creditservice.model.enums.ClientType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditRequest {
    @NotNull
    private String clientId;

    private ClientType clientType;

    @NotNull
    private Double amount;

    @NotNull
    private Double interestRate;

    @NotNull
    private LocalDate grantDate;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    private Double latePaymentInterestRate;
}