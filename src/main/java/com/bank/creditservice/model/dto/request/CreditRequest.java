package com.bank.creditservice.model.dto.request;

import com.bank.creditservice.model.enums.ClientType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Double annualInterestRate;

    @NotNull
    @Min(value = 2)
    private Integer numberOfPayments;
}